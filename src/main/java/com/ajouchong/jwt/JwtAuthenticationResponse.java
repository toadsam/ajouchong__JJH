package com.ajouchong.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtAuthenticationResponse {
    private String tokenType = "Bearer";
    private String accessToken;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
