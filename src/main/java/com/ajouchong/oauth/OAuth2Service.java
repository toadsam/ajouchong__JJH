package com.ajouchong.oauth;

import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.repository.MemberRepository;
import com.ajouchong.service.MemberService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2Service {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleOAuthApi googleOAuthApi;
    private final GoogleResourceApi googleResourceApi;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    public String devSocialLogin(String code) {
        // Access Token 획득
        String accessToken = getAccessToken(code);

        // Google 사용자 정보 가져오기
        GoogleResourceDto googleResourceDto = getUserResource(accessToken);
        String email = googleResourceDto.getEmail();
        String provider = "google";


        // 회원 존재 여부 확인 및 처리
        if (!memberService.checkLoginIdDuplicate(email)) {
            memberService.registerSocialUser(googleResourceDto, provider);
            return createLoginResponse(email, "회원가입 및 로그인에 성공했습니다.");
        }

        // 기존 회원 -> 로그인 처리
        return createLoginResponse(email, "로그인에 성공했습니다.");
    }

    private GoogleResourceDto getUserResource(String accessToken) {
        try {
            return googleResourceApi.googleGetResource("Bearer " + accessToken);
        } catch (FeignException e) {
            throw new IllegalArgumentException("Google 사용자 정보 요청 중 문제가 발생했습니다.", e);
        }
    }

    private String getAccessToken(String authorizationCode) {
        try {
            // Access Token 요청
            GoogleTokenDto tokenDto = googleOAuthApi.googleGetToken(
                    authorizationCode,
                    clientId,
                    clientSecret,
                    redirectUri,
                    "authorization_code"
            );
            return tokenDto.getAccessToken();
        } catch (FeignException e) {
            throw new IllegalArgumentException("OAuth 서버와 통신 중 문제가 발생했습니다.", e);
        }
    }

    public Boolean checkSignUp(String email) {
        return memberRepository.existsByLoginId(email);
    }

    private String createLoginResponse(String email, String message) {
        String jwtToken = jwtTokenProvider.createJwt(email, "USER", 1000L * 60 * 60 * 24); // 24시간 유효기간
        return String.format("%s, %s", message, jwtToken);
    }
}
