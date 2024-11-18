package com.ajouchong.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/login/oauth2/code/google")
    public String handleGoogleLogin(OAuth2AuthenticationToken authentication) {
        // 사용자 정보 가져오기
        String email = authentication.getPrincipal().getAttribute("email");

        // 이메일이 @ajou.ac.kr 도메인인지 확인
        if (email != null && email.endsWith("@ajou.ac.kr")) {
            return "로그인 성공: " + email;
        } else {
            return "허용되지 않은 이메일 도메인입니다.";
        }
    }
}

