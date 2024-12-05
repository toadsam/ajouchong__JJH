package com.ajouchong.controller.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/img")
public class ImageController {

    @Value("${DIR}")
    private String fileDirPath; // 이미지 저장 디렉토리 경로

    private final ResourceLoader resourceLoader;

    public ImageController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    // 이미지 반환
    @GetMapping("/{type}/{filename:.+}")
    public ResponseEntity<byte[]> getImage(
            @PathVariable String type,
            @PathVariable String filename
    ) {
        try {
            // 파일 경로 생성
            Path filePath = Paths.get(fileDirPath, type, filename);
            File file = filePath.toFile();

            // 파일이 존재하지 않으면 404 반환
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 파일 MIME 타입 추출
            String mimeType = Files.probeContentType(filePath);
            byte[] fileContent = Files.readAllBytes(filePath);

            // HTTP 응답 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, mimeType);

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
