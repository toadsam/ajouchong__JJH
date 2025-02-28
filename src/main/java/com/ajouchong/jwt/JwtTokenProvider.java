package com.ajouchong.jwt;

import com.ajouchong.entity.enumClass.MemberRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenValidity = 1000L * 60 * 60 * 24; // 1일
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일
    private static final String COOKIE_NAME = "jwt_token"; // JWT를 저장할 쿠키 이름

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String createAccessToken(String email, MemberRole role) {
        return generateToken(email, role.name(), accessTokenValidity);
    }

    public String createRefreshToken(String email) {
        return generateToken(email, null, refreshTokenValidity);
    }

    private String generateToken(String email, String role, long validity) {
        Claims claims = Jwts.claims().setSubject(email);
        Optional.ofNullable(role).ifPresent(r -> claims.put("role", r));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isExpired(String token) {
        try {
            return getClaimsFromToken(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // 만료
        } catch (JwtException e) {
            throw new InvalidJwtException("유효하지 않은 JWT 토큰입니다.");
        }
    }

    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** ✅ JWT를 쿠키에 저장하는 메서드 추가 */
    public void setJwtCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)  // XSS 공격 방지
                .secure(true)    // HTTPS에서만 전송
                .path("/")       // 모든 경로에서 사용 가능
                .maxAge(accessTokenValidity / 1000) // 초 단위로 설정
                .sameSite("Strict") // CSRF 방지
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void clearJwtCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 즉시 만료
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static class InvalidJwtException extends RuntimeException {
        public InvalidJwtException(String message) {
            super(message);
        }
    }
}
