package com.ajouchong.controller;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.oauth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final OAuth2Service oAuth2Service;

    @GetMapping("/dev/login/oauth/{registration}")
    public ApiResponse<?> devSocialLogin(@RequestParam String code, @PathVariable String registration) {
        String loginMessage = oAuth2Service.devSocialLogin(code);
        return new ApiResponse<>(1,  loginMessage, null);
    }
}

