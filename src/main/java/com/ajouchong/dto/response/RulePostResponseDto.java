package com.ajouchong.dto.response;

import com.ajouchong.entity.RulePost;
import com.ajouchong.entity.enumClass.RuleType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class RulePostResponseDto {
    private Long rPostId;
    private String rpTitle;
    private String rpContent;
    private String attachmentUrl;
    private RuleType ruleType;
    private int rpUserLikeCnt;
    private int rpHitCnt;
    private LocalDateTime rpCreateTime;
    private LocalDateTime rpUpdateTime;

    public RulePostResponseDto(RulePost rulePost) {
        this.rPostId = rulePost.getRPostId();
        this.rpTitle = rulePost.getRpTitle();
        this.rpContent = rulePost.getRpContent();
        this.attachmentUrl = rulePost.getAttachmentUrl();
        this.ruleType = rulePost.getType();
        this.rpHitCnt = rulePost.getRpHitCnt();
        this.rpUserLikeCnt = rulePost.getRpUserLikeCnt();
        this.rpCreateTime = rulePost.getRpCreateTime();
        this.rpUpdateTime = rulePost.getRpUpdateTime();
    }
}
