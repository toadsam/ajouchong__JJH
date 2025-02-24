package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.entity.Member;
import com.ajouchong.entity.enumClass.MemberRole;
import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.oauth.GoogleOAuthService;
import com.ajouchong.oauth.GoogleUserDto;
import com.ajouchong.oauth.OAuthRequestDto;
import com.ajouchong.oauth.OAuthResponseDto;
import com.ajouchong.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/login/auth", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final GoogleOAuthService googleOAuthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping("/oauth")
    public ApiResponse<OAuthResponseDto> googleLogin(@RequestBody OAuthRequestDto request) {

        GoogleUserDto googleUser = googleOAuthService.getUserInfo(request.getAccessToken());

        Member member = memberRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> {
                    Member newMember = new Member(googleUser);
                    newMember.setRole(MemberRole.STUDENT); // 기본 역할 지정
                    return memberRepository.save(newMember);
                });

        String jwtToken = jwtTokenProvider.createJwt(member.getEmail(), member.getRole());

        OAuthResponseDto responseDto = new OAuthResponseDto(jwtToken, member);

        return new ApiResponse<>(1, "Google login 성공", responseDto);
    }

    @GetMapping("/info")
    public ApiResponse<OAuthResponseDto> getUserInfo(@RequestHeader("Authorization") String jwtToken) {

        String email = jwtTokenProvider.getEmailFromToken(jwtToken.replace("Bearer ", ""));
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

        OAuthResponseDto responseDto = new OAuthResponseDto(jwtToken, member);

        return new ApiResponse<>(1, "회원 정보 조회 성공", responseDto);
    }
}

