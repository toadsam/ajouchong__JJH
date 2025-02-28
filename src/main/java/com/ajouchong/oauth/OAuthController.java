package com.ajouchong.oauth;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.entity.Member;
import com.ajouchong.entity.enumClass.MemberRole;
import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
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
    public ApiResponse<OAuthResponseDto> googleLogin(@RequestBody Map<String, String> requestBody, HttpServletResponse response) {
        log.info("🔵 [Google Login] 요청 수신: {}", requestBody);

        String accessToken = requestBody.get("accessToken");
        String jwtToken = requestBody.get("jwtToken"); // 클라이언트에서 기존 JWT 전송

        if (accessToken == null || accessToken.isEmpty()) {
            log.warn("AccessToken이 없습니다.");
            throw new IllegalArgumentException("AccessToken is missing.");
        }

        GoogleUserDto googleUser = googleOAuthService.getUserInfo(accessToken);

        Member member = memberRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> {
                    Member newMember = new Member(googleUser);
                    newMember.setRole(MemberRole.STUDENT); // 기본 역할 지정
                    return memberRepository.save(newMember);
                });

        boolean isNewTokenIssued = false;

        if (jwtToken != null && jwtTokenProvider.isExpired(jwtToken)) {
            log.warn("기존 JWT가 만료됨. 새로 발급합니다.");
            jwtToken = jwtTokenProvider.createRefreshToken(member.getEmail());
            jwtTokenProvider.setJwtCookie(response, jwtToken);
            isNewTokenIssued = true;
        } else if (jwtToken == null) {
            log.info("클라이언트에서 JWT 없음. 새로 발급 중...");
            jwtToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole());
            jwtTokenProvider.setJwtCookie(response, jwtToken);
            isNewTokenIssued = true;
        }

        log.info("신규 JWT 발급 완료: {}", jwtToken);

        OAuthResponseDto responseDto = new OAuthResponseDto(jwtToken, member);
        log.debug("responseDto: {}", responseDto);

        return new ApiResponse<>(isNewTokenIssued ? 2 : 1, "Google login 성공", responseDto);
    }

    @GetMapping("/info")
    public ApiResponse<OAuthResponseDto> getUserInfo(@RequestHeader("Authorization") String jwtToken, HttpServletResponse response) {
        log.info("🔵 [회원 정보 조회] 요청 수신. Authorization 헤더: {}", jwtToken);

        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            log.warn("Authorization 헤더가 없거나 형식이 올바르지 않음.");
            return new ApiResponse<>(0, "Invalid or missing token", null);
        }

        String token = jwtToken.replace("Bearer ", "");

        if (jwtTokenProvider.isExpired(token)) {

            String email = jwtTokenProvider.getEmailFromToken(token);
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.error("회원 정보 없음: {}", email);
                        return new RuntimeException("회원 정보가 없습니다.");
                    });

            String newToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole());
            jwtTokenProvider.setJwtCookie(response, newToken);

            log.info("새로운 JWT 발급 완료: {}", newToken);

            OAuthResponseDto responseDto = new OAuthResponseDto(newToken, member);
            return new ApiResponse<>(2, "토큰이 만료되어 재발급되었습니다.", responseDto);
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("회원 정보 없음: {}", email);
                    return new RuntimeException("회원 정보가 없습니다.");
                });

        OAuthResponseDto responseDto = new OAuthResponseDto(jwtToken, member);

        return new ApiResponse<>(1, "회원 정보 조회 성공", responseDto);
    }
}
