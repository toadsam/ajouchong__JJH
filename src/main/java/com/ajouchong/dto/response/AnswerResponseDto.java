package com.ajouchong.dto.response;

import com.ajouchong.entity.Answer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class AnswerResponseDto {
    private Long answerId;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public AnswerResponseDto(Answer answer) {
        this.answerId = answer.getAnswerId();
        this.content = answer.getContent();
        this.createTime = answer.getCreateTime();
        this.updateTime = answer.getUpdateTime();
    }
}
