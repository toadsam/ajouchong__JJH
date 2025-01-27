package com.ajouchong.service;

import com.ajouchong.dto.request.PartnershipRequestDto;
import com.ajouchong.dto.response.PartnershipResponseDto;
import com.ajouchong.entity.Partnership;
import com.ajouchong.repository.PartnershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnershipService {
    private final PartnershipRepository partnershipRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public PartnershipResponseDto savePartnership(PartnershipRequestDto requestDto) throws IOException {
        Partnership partnership = new Partnership();

        partnership.setPsTitle(requestDto.getTitle());
        partnership.setPsContent(requestDto.getContent());
        partnership.setPsCreateTime(LocalDateTime.now());
        partnership.setPsUpdateTime(LocalDateTime.now());

        List<String> imageUrls = new ArrayList<>();
        if (requestDto.getImageFiles() != null && !requestDto.getImageFiles().isEmpty()) {
            for (MultipartFile file : requestDto.getImageFiles()) {
                String image = s3UploadService.saveFile(file); // S3에 업로드 후 URL 반환
                imageUrls.add(image);
            }
        }

        partnership.setImageUrls(imageUrls);
        Partnership savedPartnership = partnershipRepository.save(partnership);

        return new PartnershipResponseDto(
                savedPartnership.getPsPostId(),
                savedPartnership.getPsTitle(),
                savedPartnership.getPsContent(),
                savedPartnership.getPsUserLikeCnt(),
                savedPartnership.getPsHitCnt(),
                savedPartnership.getPsCreateTime(),
                savedPartnership.getPsUpdateTime(),
                imageUrls
        );
    }

//    @Transactional
//    public PartnershipResponseDto changePartnership(Long id, PartnershipRequestDto requestDto) {
//        Partnership partnership = partnershipRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException(id + "번 게시글을 찾을 수 없습니다."));
//
//        partnership.setPsTitle(requestDto.getTitle());
//        partnership.setPsContent(requestDto.getContent());
//        partnership.setPsUpdateTime(LocalDateTime.now());
//
//        List<PartnershipImage> existingImages = partnership.getImages();
//
//        existingImages.clear();
//
//        for (int i = 0; i < requestDto.getImageUrls().size(); i++) {
//            String imageUrl = requestDto.getImageUrls().get(i);
//            PartnershipImage newImage = new PartnershipImage();
//            newImage.setImageUrl(imageUrl);
//            newImage.setImageOrder(i);
//            newImage.setPartnership(partnership);
//            existingImages.add(newImage);
//        }
//
//        partnershipRepository.save(partnership);
//        return convertToDto(partnership);
//    }

    public List<PartnershipResponseDto> getLatestPartnerships(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "psCreateTime"));
        Page<Partnership> partnerships = partnershipRepository.findAllByOrderByPsCreateTimeDesc(pageable);

        return partnerships.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PartnershipResponseDto getPartnershipById(Long id) {
        Partnership partnership = partnershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시글을 찾을 수 없습니다."));

        return convertToDto(partnership);
    }

    @Transactional
    public void deletePartnership(Long id) {
        partnershipRepository.deleteById(id);
    }

    @Transactional
    public void increaseLikeCount(Long id) {
        Partnership partnership = partnershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시글을 찾을 수 없습니다."));
        partnership.setPsUserLikeCnt(partnership.getPsUserLikeCnt() + 1);

        partnershipRepository.save(partnership);
    }

    @Transactional
    public void increaseHitCount(Long id) {
        Partnership partnership = partnershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시글을 찾을 수 없습니다."));
        partnership.setPsHitCnt(partnership.getPsHitCnt() + 1);

        partnershipRepository.save(partnership);
    }

    private PartnershipResponseDto convertToDto(Partnership partnership) {
        return new PartnershipResponseDto(partnership);
    }
}
