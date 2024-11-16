package com.ajouchong.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EventRequestDto {
    private Long eventId;
    private String title;
    private String date;
}
