package com.ajouchong.entity.enumClass;

import org.springframework.security.core.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {
    ADMIN,
    STUDENT,
    COUNCIL;

    @Override
    public String getAuthority() {
        return name();
    }
}
