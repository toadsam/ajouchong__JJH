package com.ajouchong.service;

import com.ajouchong.entity.Member;
import com.ajouchong.entity.enumClass.MemberRole;
import com.ajouchong.oauth.GoogleResourceDto;
import com.ajouchong.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean checkLoginIdDuplicate(String email) {
        return memberRepository.existsByLoginId(email);
    }

    public void registerSocialUser(GoogleResourceDto googleResourceDto, String provider) {
        String email = googleResourceDto.getEmail();
        String providerId = googleResourceDto.getId();
        String name = googleResourceDto.getNickname();

        // 중복 확인
        if (memberRepository.existsByLoginId(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 소셜 사용자 정보 저장 (비밀번호는 더미 값으로 설정)
        Member member = Member.builder()
                .loginId(email)
                .password(bCryptPasswordEncoder.encode("SOCIAL_LOGIN_PASSWORD")) // 소셜 로그인 사용자는 더미 비밀번호 사용
                .name(name) // 사용자 이름 설정
                .role(MemberRole.STUDENT) // 기본 역할 설정
                .provider(provider) // 소셜 제공자 (google 등)
                .providerId(providerId) // 소셜 제공자 고유 ID
                .build();

        System.out.println("Saving Member: " + member);

        memberRepository.save(member);
    }

    public Member getLoginMemberById(String loginId) {
        if (loginId == null) {
            throw new IllegalArgumentException("로그인 ID가 비어 있습니다.");
        }

        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 ID가 '" + loginId + "'인 사용자를 찾을 수 없습니다."));
    }
}