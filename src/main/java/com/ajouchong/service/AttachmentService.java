package com.ajouchong.service;

import com.ajouchong.entity.Attachment;
import com.ajouchong.entity.enumClass.AttachmentType;
import com.ajouchong.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentComponent attachmentComponent;

    @Transactional
    public List<Attachment> saveAttachments(Map<AttachmentType, List<MultipartFile>> multipartFileListMap) throws IOException {
        List<Attachment> imageFiles = attachmentComponent.storeFiles(multipartFileListMap.get(AttachmentType.IMAGE), AttachmentType.IMAGE);
        List<Attachment> generalFiles = attachmentComponent.storeFiles(multipartFileListMap.get(AttachmentType.GENERAL), AttachmentType.GENERAL);

        return Stream.of(imageFiles, generalFiles)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Map<AttachmentType, List<Attachment>> findAttachments() {
        List<Attachment> attachments = attachmentRepository.findAll();

        return attachments.stream()
                .collect(Collectors.groupingBy(Attachment::getAttachmentType));
    }
}
