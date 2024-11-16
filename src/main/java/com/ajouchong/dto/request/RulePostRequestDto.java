package com.ajouchong.dto.request;

import com.ajouchong.entity.enumClass.RuleType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RulePostRequestDto {
    private String rpTitle;
    private String rpContent;
    private String attachmentUrl;
    private RuleType ruleType;
}
