package com.ajouchong.service;

import com.ajouchong.dto.request.JoinRequestDto;
import com.ajouchong.dto.request.LoginRequestDto;
import com.ajouchong.entity.Member;
import com.ajouchong.entity.enumClass.MemberRole;
import com.ajouchong.oauth.GoogleResourceDto;
import com.ajouchong.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean checkLoginIdDuplicate(String email){
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

        memberRepository.save(member);
    }



    public Member login(LoginRequestDto loginRequest) {
        // loginId로 회원 검색
        Optional<Member> findMember = memberRepository.findByLoginId(loginRequest.getLoginId());

        // 회원이 존재하지 않으면 null 반환
        if (findMember.isEmpty()) {
            return null;
        }

        // 비밀번호가 일치하지 않으면 null 반환
        Member member = findMember.get();
        if (!member.getPassword().equals(loginRequest.getPassword())) {
            return null;
        }

        return member;
    }

    public Member getLoginMemberById(String memberId) {
        if (memberId == null) return null;

        Optional<Member> findMember = memberRepository.findByLoginId(memberId);
        return findMember.orElse(null);
    }

    public void securityJoin(JoinRequestDto joinRequest){
        if(memberRepository.existsByLoginId(joinRequest.getLoginId())){
            return;
        }

        joinRequest.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));
        memberRepository.save(joinRequest.toEntity());
    }

}
