package com.example.dev.feign;

import com.example.dev.config.FeignFormSupportConfig;
import com.example.dev.dto.request.ExchangeTokenRequest;
import com.example.dev.dto.response.ExchangeTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@FeignClient(
        name = "outbound-identity",
        url = "https://oauth2.googleapis.com",
        configuration = FeignFormSupportConfig.class
)
public interface OutboundIdentityClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeCodeForToken(@ModelAttribute ExchangeTokenRequest request);
}
