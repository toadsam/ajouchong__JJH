package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.oauth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/api/login/oauth", produces = "application/json")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/google")
    public ApiResponse<String> loginWithGoogle(@RequestParam String code) {
        return oAuth2Service.socialLogin(code);
    }
}

