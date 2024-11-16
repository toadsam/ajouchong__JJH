package com.ajouchong.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PartnershipRequestDto {
    private String title;
    private String content;
    private List<String> imageUrls;
}
