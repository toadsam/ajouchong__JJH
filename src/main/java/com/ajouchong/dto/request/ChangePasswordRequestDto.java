package com.ajouchong.dto.request;

import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {
    private String oldPassword;
    private String newPassword;
}
