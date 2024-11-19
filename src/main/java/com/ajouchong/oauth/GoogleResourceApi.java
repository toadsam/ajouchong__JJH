package com.ajouchong.oauth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "GoogleResource", url = "https://www.googleapis.com")
public interface GoogleResourceApi {
    @GetMapping(value = "/oauth2/v2/userinfo")
    GoogleResourceDto googleGetResource(
            @RequestHeader("Authorization") String bearerToken);
}


