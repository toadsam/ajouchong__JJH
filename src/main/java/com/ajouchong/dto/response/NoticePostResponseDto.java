package com.ajouchong.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class NoticePostResponseDto {
    private Long nPost_id;
    private String npTitle;
    private String npContent;
    private int npUserLikeCnt;
    private int npHitCnt;
    private LocalDateTime npCreateTime;
    private LocalDateTime npUpdateTime;
    private List<String> imageUrls;     // 이미지 파일 URL
    private List<String> generalUrls;   // 일반 파일 URL
}

