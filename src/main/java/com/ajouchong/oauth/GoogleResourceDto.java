package com.ajouchong.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class GoogleResourceDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String nickname;

    @Builder
    public GoogleResourceDto(String id, String email, String nickname){
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
