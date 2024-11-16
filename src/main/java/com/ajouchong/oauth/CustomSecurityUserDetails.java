package com.ajouchong.oauth;

import com.ajouchong.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomSecurityUserDetails implements UserDetails {

    private final Member user;
    public CustomSecurityUserDetails(Member user) {
        this.user = user;
    }

    // 현재 user의 role을 반환 (ex. "ROLE_ADMIN" / "ROLE_USER" 등)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                // 앞에 "ROLE_" 접두사 필수 !
                return "ROLE_" + user.getRole().name();
            }
        });

        return collection;
    }

    // user의 비밀번호 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // user의 username 반환
    @Override
    public String getUsername() {
        return user.getLoginId();
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