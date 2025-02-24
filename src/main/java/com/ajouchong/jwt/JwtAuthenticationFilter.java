package com.ajouchong.jwt;

import com.ajouchong.entity.Member;
import com.ajouchong.entity.enumClass.MemberRole;
import com.ajouchong.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Authorization 헤더 추출
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (!isValidAuthorizationHeader(authorization)) {
            // Authorization 헤더가 없거나 잘못된 경우 다음 필터로 요청 전달
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 접두사 제거 후 토큰 추출
        String token = authorization.substring(7);

        try {
            // 토큰 만료 여부 확인
            if (jwtTokenProvider.isExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has expired.");
                return;
            }

            // 인증 정보 설정
            setAuthentication(token);
        } catch (Exception e) {
            // 토큰 검증 실패 시 예외 처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + e.getMessage() + "\"}");
            return;
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthorizationHeader(String authorization) {
        return authorization != null && authorization.startsWith("Bearer ");
    }

    private void setAuthentication(String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        String role = jwtTokenProvider.getRoleFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email + " 사용자를 찾을 수 없습니다."));

        member.setRole(MemberRole.valueOf(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name()))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
