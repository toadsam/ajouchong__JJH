package com.ajouchong.oauth;

import com.ajouchong.entity.enumClass.MemberRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class GoogleUserDetails implements UserDetails {

    private final String loginId;
    private final MemberRole role;

    public GoogleUserDetails(String loginId, MemberRole role) {
        this.loginId = loginId;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인 사용자의 경우 비밀번호를 사용하지 않음
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
