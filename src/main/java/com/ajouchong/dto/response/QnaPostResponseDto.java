package com.ajouchong.dto.response;

import com.ajouchong.entity.QnaPost;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class QnaPostResponseDto {
    private Long qPostId;
    private String qpTitle;
    private String qpContent;
    private boolean isReplied = false;
    private int qpUserLikeCnt;
    private int qpHitCnt;
    private LocalDateTime qpCreateTime;
    private LocalDateTime qpUpdateTime;
    private AnswerResponseDto answer;

    public QnaPostResponseDto(QnaPost post) {
        this.qPostId = post.getQPostId();
        this.qpTitle = post.getQpTitle();
        this.qpContent = post.getQpContent();
        this.isReplied = post.isReplied();
        this.qpUserLikeCnt = post.getQpUserLikeCnt();
        this.qpHitCnt = post.getQpHitCnt();
        this.qpCreateTime = post.getQpCreateTime();
        this.qpUpdateTime = post.getQpUpdateTime();

        if (post.getAnswer() != null) {
            this.answer = new AnswerResponseDto(post.getAnswer());
        }

    }
}
