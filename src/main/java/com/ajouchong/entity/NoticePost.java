package com.ajouchong.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter @Setter
public class NoticePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nPostId;

    private String npTitle;
    private String npContent;

    @OneToMany(mappedBy = "noticePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    private int npUserLikeCnt = 0;
    private int npHitCnt = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member author;

    private LocalDateTime npCreateTime;
    private LocalDateTime npUpdateTime;

    @PrePersist
    protected void onCreate() {
        this.npCreateTime = LocalDateTime.now();
        this.npUpdateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.npUpdateTime = LocalDateTime.now();
    }

    @Builder
    public NoticePost(String npTitle, String npContent, Member author, List<Attachment> attachments) {
        this.npTitle = npTitle;
        this.npContent = npContent;
        this.author = author;
        this.attachments = (attachments != null) ? attachments : new ArrayList<>();
        this.npHitCnt = 0;
        this.npUserLikeCnt = 0;
    }
}
