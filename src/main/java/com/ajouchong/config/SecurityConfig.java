package com.ajouchong.config;

import com.ajouchong.entity.enumClass.MemberRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 접근 권한 설정
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/favicon.ico").permitAll()
                        .requestMatchers("/oauth-login/admin").hasRole(MemberRole.ADMIN.name()) // ADMIN 권한 필요
                        .requestMatchers("/oauth-login/info").authenticated() // 인증 필요
                        .anyRequest().permitAll() // 그 외 요청 허용
                );

        // OAuth 2.0 로그인 방식 설정
        http
                .oauth2Login(auth -> auth
                        .loginPage("/oauth-login/login") // 사용자 지정 로그인 페이지
                        .defaultSuccessUrl("/oauth-login") // 로그인 성공 후 리다이렉트
                        .failureUrl("/oauth-login/login") // 로그인 실패 시 리다이렉트
                        .permitAll()
                );

        // 로그아웃 설정
        http
                .logout(auth -> auth
                        .logoutUrl("/oauth-login/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트
                        .permitAll()
                );

        http
                .csrf(AbstractHttpConfigurer::disable);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}


