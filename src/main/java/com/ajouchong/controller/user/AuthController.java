package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.response.ProfileResponseDto;
import com.ajouchong.entity.Member;
import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @GetMapping("/profile")
    public ApiResponse<ProfileResponseDto> profile() {
        // 현재 인증된 사용자
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        Object principal = authentication.getPrincipal();

        // `principal`이 `Member` 객체인지 확인 후 `loginId` 추출
        if (principal instanceof Member member) {
            // Member 객체로 캐스팅
            String loginId = member.getLoginId(); // loginId 추출

            // 사용자 정보를 데이터베이스에서 조회
            Member dbMember = memberService.getLoginMemberById(loginId);
            if (dbMember == null) {
                throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
            }

            // 응답 생성
            ProfileResponseDto profile = ProfileResponseDto.builder()
                    .name(dbMember.getName())
                    .email(dbMember.getLoginId())
                    .role(dbMember.getRole().name())
                    .build();

            return new ApiResponse<>(1, "사용자 정보 조회 성공", profile);
        } else {
            throw new IllegalArgumentException("유효하지 않은 사용자 세션입니다.");
        }
    }


}
