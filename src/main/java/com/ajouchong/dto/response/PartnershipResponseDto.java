package com.ajouchong.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnershipResponseDto {
    private Long psPostId;
    private String psTitle;
    private String psContent;
    private int psUserLikeCnt;
    private int psHitCnt;
    private LocalDateTime psCreateTime;
    private LocalDateTime psUpdateTime;
    private List<String> imageUrls;
}
