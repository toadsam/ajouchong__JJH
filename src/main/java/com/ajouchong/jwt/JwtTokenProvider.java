package com.ajouchong.jwt;

import com.ajouchong.entity.Member;
import com.ajouchong.repository.MemberRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final MemberRepository memberRepository;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, MemberRepository memberRepository) {
        // SecretKeySpec을 사용하여 HMAC-SHA256 알고리즘 키 생성
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        this.memberRepository = memberRepository;
    }

    // loginId 반환 메서드
    public String getLoginId(String token) {
        Claims claims = parseClaims(token);
        String loginId = claims.get("sub", String.class);
        return loginId;
    }

    // role 반환 메서드
    public String getRole(String token) {
        Claims claims = parseClaims(token);
        return claims.get("role", String.class);
    }

    // 토큰이 소멸(유효기간 만료)하였는지 검증 메서드
    public Boolean isExpired(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().before(new Date());
    }

    public Member getUserFromToken(String token) {
        String email = getLoginId(token);
        return memberRepository.findByLoginId(email)
                .orElseThrow(() -> new IllegalArgumentException("로그인 ID가 '" + email + "'인 사용자를 찾을 수 없습니다."));
    }

    // 토큰 생성 메서드
    public String createJwt(String loginId, String role, Long expiredMs) {
        String jwt = Jwts.builder()
                .setSubject(loginId)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }


    // 토큰 파싱 메서드
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("토큰이 만료되었습니다.", e);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("잘못된 토큰 형식입니다.", e);
        } catch (JwtException e) {
            throw new IllegalArgumentException("토큰 처리 중 오류가 발생했습니다.", e);
        }
    }

}
