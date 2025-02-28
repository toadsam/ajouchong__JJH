package com.ajouchong.oauth;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.entity.Member;
import com.ajouchong.entity.enumClass.MemberRole;
import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/login/auth", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final GoogleOAuthService googleOAuthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping("/oauth")
    public ApiResponse<OAuthResponseDto> googleLogin(@RequestBody Map<String, String> requestBody) {
        String accessToken = requestBody.get("accessToken");
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("AccessToken is missing.");
        }

        GoogleUserDto googleUser = googleOAuthService.getUserInfo(accessToken);

        Member member = memberRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> {
                    Member newMember = new Member(googleUser);
                    newMember.setRole(MemberRole.STUDENT); // 기본 역할 지정
                    return memberRepository.save(newMember);
                });

        String jwtToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole());
        OAuthResponseDto responseDto = new OAuthResponseDto(jwtToken, member);

        log.debug("responseDto: {}", responseDto);

        return new ApiResponse<>(1, "Google login 성공", responseDto);
    }

    @GetMapping("/info")
    public ApiResponse<OAuthResponseDto> getUserInfo(@RequestHeader("Authorization") String jwtToken) {
        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            return new ApiResponse<>(0, "Invalid or missing token", null);
        }

        String token = jwtToken.replace("Bearer ", "");

        if (jwtTokenProvider.isExpired(token)) {
            return new ApiResponse<>(0, "Token has expired", null);
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

        OAuthResponseDto responseDto = new OAuthResponseDto(jwtToken, member);

        return new ApiResponse<>(1, "회원 정보 조회 성공", responseDto);
    }
}
