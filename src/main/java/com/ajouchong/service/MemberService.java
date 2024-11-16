package com.ajouchong.service;

import com.ajouchong.dto.request.JoinRequestDto;
import com.ajouchong.dto.request.LoginRequestDto;
import com.ajouchong.entity.Member;
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

    public boolean checkLoginIdDuplicate(String loginId){
        return memberRepository.existsByLoginId(loginId);
    }

    public void join(JoinRequestDto joinRequest) {
        memberRepository.save(joinRequest.toEntity());
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
