package com.ajouchong.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class GoogleResourceDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("name")
    private String nickname;

    @Builder
    public GoogleResourceDto(String id, String email, String picture, String nickname){
        this.id = id;
        this.email = email;
        this.picture = picture;
        this.nickname = nickname;
    }
}
