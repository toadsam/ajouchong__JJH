package com.ajouchong.entity;

import com.ajouchong.entity.enumClass.AttachmentType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter  @Setter
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String originFilename;
    private String storeFilename;

    @Enumerated(EnumType.STRING)
    private AttachmentType attachmentType;

    @ManyToOne
    @JoinColumn(name = "nPostId")
    private NoticePost noticePost;

    @Builder
    public Attachment(String originFilename, String storeFilename, AttachmentType attachmentType) {
        this.originFilename = originFilename;
        this.storeFilename = storeFilename;
        this.attachmentType = attachmentType;
    }
}

