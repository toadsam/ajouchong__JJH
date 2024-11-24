package com.ajouchong.oauth;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.service.MemberService;
import com.fasterxml.jackson.databind.JsonNode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2Service {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.client-secret}")
    private String clientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.google.token-uri}")
    private String tokenUri;

    @Value("${oauth2.google.resource-uri}")
    private String resourceUri;

    public ApiResponse<String> socialLogin(String code) {
        // Access Token 획득
        String accessToken = getAccessToken(code);

        // Google 사용자 정보 가져오기
        GoogleResourceDto googleResourceDto = getUserInfoFromGoogle(accessToken);
        String email = googleResourceDto.getEmail();
        String nickname = googleResourceDto.getNickname();
        String provider = "google";

        // 신규 사용자 등록
        if (!memberService.checkLoginIdDuplicate(email)) {
            memberService.registerSocialUser(googleResourceDto, provider);
            return createLoginResponse(email, "회원가입 및 로그인에 성공했습니다." + "email=" + email + ",nickname=" + nickname);
        }

        return createLoginResponse(email, "로그인에 성공했습니다.");
    }

    private String getAccessToken(String code) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

            ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
            JsonNode accessTokenNode = responseNode.getBody();
            assert accessTokenNode != null;
            return accessTokenNode.get("access_token").asText();
        } catch (FeignException e) {
            throw new IllegalArgumentException("OAuth 서버와 통신 중 문제가 발생했습니다.", e);
        }
    }

    public GoogleResourceDto getUserInfoFromGoogle(String accessToken) {
        // 헤더에 Authorization 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        // Google 사용자 정보 API 호출
        try {
            ResponseEntity<GoogleResourceDto> response = restTemplate.exchange(
                    resourceUri,
                    HttpMethod.GET,
                    entity,
                    GoogleResourceDto.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("Google 사용자 정보 요청 중 문제가 발생했습니다.", e);
        }
    }

    private ApiResponse<String> createLoginResponse(String email, String message) {
        String jwtToken = jwtTokenProvider.createJwt(email, "STUDENT", 1000L * 60 * 60 * 24); // 24시간 유효
        return new ApiResponse<>(1, message, jwtToken);
    }
}
