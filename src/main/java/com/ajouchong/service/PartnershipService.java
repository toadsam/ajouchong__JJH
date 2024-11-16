package com.ajouchong.service;

import com.ajouchong.dto.request.PartnershipRequestDto;
import com.ajouchong.dto.response.PartnershipResponseDto;
import com.ajouchong.entity.Partnership;
import com.ajouchong.entity.PartnershipImage;
import com.ajouchong.repository.PartnershipImageRepository;
import com.ajouchong.repository.PartnershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnershipService {
    private final PartnershipRepository partnershipRepository;
    private final PartnershipImageRepository partnershipImageRepository;

    @Transactional
    public PartnershipResponseDto savePartnership(PartnershipRequestDto requestDto){
        Partnership partnership = new Partnership();

        partnership.setPsTitle(requestDto.getTitle());
        partnership.setPsContent(requestDto.getContent());
        partnership.setPsCreateTime(LocalDateTime.now());
        partnership.setPsUpdateTime(LocalDateTime.now());

        Partnership savedPartnership = partnershipRepository.save(partnership);

        List<PartnershipImage> images = new ArrayList<>();
        for (int i = 0; i < requestDto.getImageUrls().size(); i++) {
            String imageUrl = requestDto.getImageUrls().get(i);
            PartnershipImage image = new PartnershipImage();
            image.setImageUrl(imageUrl);
            image.setImageOrder(i);
            image.setPartnership(savedPartnership);
            images.add(image);
        }

        partnershipImageRepository.saveAll(images);

        List<String> imageUrls = images.stream().map(PartnershipImage::getImageUrl).collect(Collectors.toList());
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

    @Transactional
    public PartnershipResponseDto changePartnership(Long id, PartnershipRequestDto requestDto) {
        Partnership partnership = partnershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시글을 찾을 수 없습니다."));

        partnership.setPsTitle(requestDto.getTitle());
        partnership.setPsContent(requestDto.getContent());
        partnership.setPsUpdateTime(LocalDateTime.now());

        List<PartnershipImage> existingImages = partnership.getImages();

        existingImages.clear();

        for (int i = 0; i < requestDto.getImageUrls().size(); i++) {
            String imageUrl = requestDto.getImageUrls().get(i);
            PartnershipImage newImage = new PartnershipImage();
            newImage.setImageUrl(imageUrl);
            newImage.setImageOrder(i);
            newImage.setPartnership(partnership);
            existingImages.add(newImage);
        }

        partnershipRepository.save(partnership);
        return convertToDto(partnership);
    }

    @Transactional(readOnly = true)
    public List<PartnershipResponseDto> getAllPartnerships() {
        List<Partnership> partnerships = partnershipRepository.findAll();

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
        List<String> imageUrls = partnership.getImages().stream()
                .map(PartnershipImage::getImageUrl)
                .collect(Collectors.toList());

        return new PartnershipResponseDto(
                partnership.getPsPostId(),
                partnership.getPsTitle(),
                partnership.getPsContent(),
                partnership.getPsUserLikeCnt(),
                partnership.getPsHitCnt(),
                partnership.getPsCreateTime(),
                partnership.getPsUpdateTime(),
                imageUrls
        );
    }
}
