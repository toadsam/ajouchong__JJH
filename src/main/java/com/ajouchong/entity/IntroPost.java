package com.ajouchong.entity;

import com.ajouchong.entity.enumClass.IntroPostPageName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "IntroPost")
@Entity
@Getter @Setter
public class IntroPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long intro_postId;

    @Enumerated(EnumType.STRING)
    private IntroPostPageName page;

    private String imageUrl;

    @Column(nullable = false, updatable = false)
    private String createdAt;
}
