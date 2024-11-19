package com.ajouchong.jwt;

import com.ajouchong.entity.enumClass.MemberRole;
import com.ajouchong.oauth.GoogleUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

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
            response.getWriter().write("Invalid token: " + e.getMessage());
            return;
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthorizationHeader(String authorization) {
        return authorization != null && authorization.startsWith("Bearer ");
    }

    private void setAuthentication(String token) {
        // 토큰에서 사용자 정보 추출
        String loginId = jwtTokenProvider.getLoginId(token);
        String role = jwtTokenProvider.getRole(token);

        // GoogleUserDetails 객체 생성
        GoogleUserDetails userDetails = new GoogleUserDetails(loginId, MemberRole.valueOf(role));

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
