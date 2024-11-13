package com.ajouchong.service;

import com.ajouchong.entity.Attachment;
import com.ajouchong.entity.enumClass.AttachmentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AttachmentComponent {

    @Value("${file.dir}")
    private String fileDirPath;

    public List<Attachment> storeFiles(List<MultipartFile> multipartFiles, AttachmentType attachmentType) throws IOException {
        List<Attachment> attachments = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                attachments.add(storeFile(multipartFile, attachmentType));
            }
        }

        return attachments;
    }

    public Attachment storeFile(MultipartFile multipartFile, AttachmentType attachmentType) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
        String fullPath = createPath(storeFilename, attachmentType);

        // 디버깅 로그 추가
        System.out.println("Full path for file storage: " + fullPath);

        Path directoryPath = Paths.get(fullPath).getParent();
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        multipartFile.transferTo(new File(fullPath));

        return Attachment.builder()
                .originFilename(originalFilename)
                .storeFilename(storeFilename)
                .attachmentType(attachmentType)
                .build();
    }


    private String createPath(String storeFilename, AttachmentType attachmentType) {
        String viaPath = (attachmentType == AttachmentType.IMAGE) ? "images/" : "generals/";
        return fileDirPath.endsWith("/") ? fileDirPath + viaPath + storeFilename : fileDirPath + "/" + viaPath + storeFilename;
    }



    private String createStoreFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + ext;
    }


    private String extractExt(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        return originalFilename.substring(idx);
    }
}