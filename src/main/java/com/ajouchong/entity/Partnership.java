package com.ajouchong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Partnership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long psPostId;

    private String psTitle;
    private String psContent;

    private int psUserLikeCnt = 0;
    private int psHitCnt = 0;

    private LocalDateTime psCreateTime;
    private LocalDateTime psUpdateTime;

    @OneToMany(mappedBy = "partnership", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartnershipImage> images = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.psCreateTime = LocalDateTime.now();
        this.psUpdateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.psUpdateTime = LocalDateTime.now();
    }
}
