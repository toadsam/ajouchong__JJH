package com.ajouchong.jwt;

import com.ajouchong.entity.Member;
import com.ajouchong.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final MemberRepository memberRepository;

    public JwtTokenProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Value("${jwt.secret}")
    private String jwt_secret;

    @Value("${jwt.expiration_time}")
    private int expiration_time;

    // 토큰 생성
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration_time);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);

        Key key = new SecretKeySpec(jwt_secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰에서 사용자 이메일 추출
    public String getUserEmailFromToken(String token) {
        Key key = new SecretKeySpec(jwt_secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Member getUserFromToken(String token) {
        String email = getUserEmailFromToken(token);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 토큰 유효성 검사
    public boolean validateToken(String authToken) {
        try {
            Key key = new SecretKeySpec(jwt_secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("유효하지 않은 JWT 토큰: " + e.getMessage());
        }
        return false;
    }

    // request에서 token 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
