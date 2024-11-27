package com.ajouchong.controller.user;

import com.ajouchong.oauth.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/login/oauth", produces = "application/json")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/google")
    public void loginWithGoogle(@RequestParam String code, HttpServletResponse response) {
        String jwtToken = oAuth2Service.socialLogin(code).getData();
        response.setHeader("Authorization", "Bearer " + jwtToken);

        // ajouchong.com으로 리다이렉트
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "https://ajouchong.com");
    }
}

