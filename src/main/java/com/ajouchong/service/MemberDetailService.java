package com.ajouchong.service;

//@RequiredArgsConstructor
//@Service
//public class MemberDetailService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
//
//        return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(), member.getAuthorities());
//    }
//}
