package com.ajouchong.dto.request;

import com.ajouchong.entity.Member;
import com.ajouchong.entity.NoticePost;
import com.ajouchong.entity.enumClass.AttachmentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class NoticePostRequestDto {
    private Member author;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
    private Map<AttachmentType, List<MultipartFile>> attachmentFiles = new ConcurrentHashMap<>();

    @Builder
    public NoticePostRequestDto(Member author, String title, String content, Map<AttachmentType, List<MultipartFile>> attachmentFiles) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.attachmentFiles = attachmentFiles;
    }

    public NoticePost createNoticePost() {
        NoticePost noticePost = new NoticePost();
        noticePost.setAuthor(this.author);
        noticePost.setNpTitle(this.title);
        noticePost.setNpContent(this.content);
        noticePost.setAttachments(new ArrayList<>());

        return noticePost;
    }
}
