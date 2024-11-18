package com.ajouchong.controller;

import com.ajouchong.entity.Member;
import com.ajouchong.entity.enumClass.MemberRole;
import com.ajouchong.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private MemberRepository memberRepository; // Member 저장소

    @GetMapping("/login/oauth2/code/google")
    public String handleGoogleLogin(OAuth2AuthenticationToken authentication) {
        // Google에서 반환한 사용자 정보 가져오기
        String email = authentication.getPrincipal().getAttribute("email");
        String name = authentication.getPrincipal().getAttribute("name");
        String googleId = authentication.getPrincipal().getAttribute("sub"); // Google 사용자 고유 ID

        // 이메일 도메인이 @ajou.ac.kr인지 확인
        if (email != null && email.endsWith("@ajou.ac.kr")) {
            // 이미 회원인지 확인
            Optional<Member> existingMember = memberRepository.findByLoginId(email);
            if (existingMember.isPresent()) {
                return "이미 등록된 회원: " + existingMember.get().getLoginId();
            }

            // 새 Member 객체 생성
            Member newMember = Member.builder()
                    .loginId(email)
                    .name(name)
                    .role(MemberRole.STUDENT) // 기본 역할 지정
                    .provider("google") // OAuth 제공자
                    .providerId(googleId) // Google 사용자 고유 ID
                    .build();

            // 데이터베이스에 저장
            memberRepository.save(newMember);

            return "회원 가입 성공: " + newMember.getName();
        } else {
            return "허용되지 않은 이메일 도메인입니다.";
        }
    }
}

