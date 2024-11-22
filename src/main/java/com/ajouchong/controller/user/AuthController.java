package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/profile")
    public ApiResponse<?> profile(@RequestHeader("Authorization") String token) {

        return new ApiResponse<>(1, "로그아웃 되었습니다.", null);
    }
}
