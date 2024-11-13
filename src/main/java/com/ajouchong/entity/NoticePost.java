package com.ajouchong.entity;

import jakarta.persistence.*;
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

}
