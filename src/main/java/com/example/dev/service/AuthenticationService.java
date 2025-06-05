package com.example.dev.service;

import com.example.dev.dto.request.*;
import com.example.dev.dto.response.AuthenticationResponse;
import com.example.dev.dto.response.ExchangeTokenResponse;
import com.example.dev.dto.response.IntrospectResponse;
import com.example.dev.entity.InvalidatedToken;
import com.example.dev.entity.User;
import com.example.dev.enums.ErrorCode;
import com.example.dev.exception.AppException;
import com.example.dev.feign.OutboundIdentityClient;
import com.example.dev.repository.InvalidatedTokenRepository;
import com.example.dev.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
  UserRepository userRepository;
  PasswordEncoder passwordEncoder;
  InvalidatedTokenRepository invalidatedTokenRepository;
  OutboundIdentityClient  outboundIdentityClient;

  @NonFinal
  @Value("${jwt.secretKey}")
  private String secret;

  @NonFinal
  @Value("${jwt.valid-expiration}")
  private long validExpiration;

  @NonFinal
  @Value("${jwt.refreshable-duration}")
  private long refreshableDuration;

  @NonFinal
  @Value("${google.client-id}")
  private String clientId;

  @NonFinal
  @Value("${google.client-secret}")
  private String clientSecret;

  @NonFinal
  @Value("${google.redirect-uri}")
  private String redirectUri;

  public AuthenticationResponse outboundAuthenticate(String code) {
    log.info("Authenticating with code: {}", code);

    // Call to Google's token exchange endpoint via Feign
    ExchangeTokenResponse response = outboundIdentityClient.exchangeCodeForToken(
            ExchangeTokenRequest.builder()
                    .code(code)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .redirectUri(redirectUri)
                    .grantType("authorization_code")
                    .build()
    );

    log.info("Token received: {}", response.getAccessToken());

    return AuthenticationResponse.builder()
            .token(response.getAccessToken())
            .authenticated(true)
            .build();
  }

  public IntrospectResponse introspect(IntrospectRequest request) {
    try {
      verifyToken(request.getToken(), false);

      return IntrospectResponse.builder().valid(true).build();
    } catch (Exception e) {
      return IntrospectResponse.builder().valid(false).build();
    }
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    log.info("Singnkey is: {}", secret);
    User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

    String token = generateToken(user);
    return AuthenticationResponse.builder().authenticated(true).token(token).build();
  }

  public void logout(LogoutRequest request) throws ParseException, JOSEException {
    SignedJWT jwt = verifyToken(request.getToken(), true); // reuse here

    String jti = jwt.getJWTClaimsSet().getJWTID();
    Date expiryTime = jwt.getJWTClaimsSet().getExpirationTime();

    if (jti == null || expiryTime == null) {
      throw new IllegalArgumentException("Invalid token: missing jti or expiration");
    }

    invalidatedTokenRepository.save(
        InvalidatedToken.builder().id(jti).expiryTime(expiryTime).build());

    log.info("Token with jti {} has been invalidated until {}", jti, expiryTime);
  }

  public AuthenticationResponse refreshToken(RefreshTokenRequest request)
      throws ParseException, JOSEException {
    SignedJWT jwt = verifyToken(request.getToken(), true); // 🔐 Validate the incoming token

    String jti = jwt.getJWTClaimsSet().getJWTID();
    Date expiryTime = jwt.getJWTClaimsSet().getExpirationTime();

    // ❌ Invalidate the used refresh token (rotation)
    invalidatedTokenRepository.save(
        InvalidatedToken.builder().id(jti).expiryTime(expiryTime).build());

    // 🔍 Load user
    String userName = jwt.getJWTClaimsSet().getSubject();
    User user =
        userRepository
            .findByUsername(userName)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    // 🔁 Generate a new access token (and refresh token optionally)
    String token = generateToken(user);

    return AuthenticationResponse.builder().authenticated(true).token(token).build();
  }

  private SignedJWT verifyToken(String token, boolean isRefresh)
      throws ParseException, JOSEException {
    SignedJWT signedJWT = SignedJWT.parse(token);
    JWSVerifier verifier = new MACVerifier(secret.getBytes());

    boolean verified = signedJWT.verify(verifier);
    Date expiryTime =
        isRefresh
            ? Date.from(
                signedJWT
                    .getJWTClaimsSet()
                    .getIssueTime()
                    .toInstant()
                    .plusSeconds(refreshableDuration))
            : signedJWT.getJWTClaimsSet().getExpirationTime();

    boolean notExpired = expiryTime != null && expiryTime.after(new Date());

    if (!verified || !notExpired) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
      throw new AppException(ErrorCode.UNAUTHENTICATED);

    return signedJWT;
  }

  private String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS256); // HS512 can't run a program

    JWTClaimsSet jwtClaimsSet =
        new JWTClaimsSet.Builder()
            .jwtID(UUID.randomUUID().toString())
            .subject(user.getUsername())
            .issuer("william-nguyen")
            .issueTime(new Date())
            .expirationTime(Date.from(Instant.now().plusSeconds(validExpiration)))
            .claim("roles", buildScope(user))
            .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);

    try {
      jwsObject.sign(new MACSigner(secret.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error("Cannot create token", e);
      throw new RuntimeException(e);
    }
  }

  private String buildScope(User user) {
    StringJoiner joiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles()
          .forEach(
              role -> {
                joiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                  role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
                }
              });
    }
    return joiner.toString();
  }
}
