package com.ajouchong.dto.request;

import com.ajouchong.entity.enumClass.MemberRole;
import lombok.Data;

@Data
public class AddMemberRequestDto {
    private String name;
    private String email;
    private String password;
    private String student_id;
    private String major;
    private MemberRole role;
}
