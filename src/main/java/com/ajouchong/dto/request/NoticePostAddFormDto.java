package com.ajouchong.dto.request;

import com.ajouchong.entity.Member;
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
public class NoticePostAddFormDto {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<MultipartFile> imageFiles;
    private List<MultipartFile> generalFiles;

    @Builder
    public NoticePostAddFormDto(String title, String content, List<MultipartFile> imageFiles, List<MultipartFile> generalFiles) {
        this.title = title;
        this.content = content;
        this.imageFiles = (imageFiles != null) ? imageFiles : new ArrayList<>();
        this.generalFiles = (generalFiles != null) ? generalFiles : new ArrayList<>();
    }

    public NoticePostRequestDto createNoticePostDto(Member member) {
        Map<AttachmentType, List<MultipartFile>> attachments = getAttachmentTypeListMap();
        return NoticePostRequestDto.builder()
                .author(member)
                .content(this.content)
                .title(this.title)
                .attachmentFiles(attachments)
                .build();
    }

    private Map<AttachmentType, List<MultipartFile>> getAttachmentTypeListMap() {
        Map<AttachmentType, List<MultipartFile>> attachments = new ConcurrentHashMap<>();
        attachments.put(AttachmentType.IMAGE, imageFiles);
        attachments.put(AttachmentType.GENERAL, generalFiles);
        return attachments;
    }
}
