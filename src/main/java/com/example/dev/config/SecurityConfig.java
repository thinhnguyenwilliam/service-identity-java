package com.example.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private static final String[] PUBLIC_URLS = {
    "/api/auth/token",
    "/api/auth/introspect",
    "/api/auth/logout",
    "/api/auth/refresh", "/api/auth/outbound/authentication",

    // endpoints are just only unit test
    //"api/users/**",
    //
  };

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
      CustomJwtDecoder customJwtDecoder)
      throws Exception {
    return http
            .cors(Customizer.withDefaults()) // Enable CORS
            .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, PUBLIC_URLS)
                    .permitAll()
                    // .requestMatchers(HttpMethod.GET, "/api/users").hasRole(Role.ADMIN.name()) //
                    // ROLE_ADMIN, Authorization following endpoint
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .jwt(
                        jwt ->
                            jwt.decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix(""); // default
    converter.setAuthoritiesClaimName("roles"); // or "scp" or whatever your JWT uses

    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
    return jwtConverter;
  }


  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "http://localhost:3000",
            "https://user.yourapp.com"
    ));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }


}
