package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.response.PartnershipResponseDto;
import com.ajouchong.service.PartnershipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/partnership")
public class PartnershipUserController {
    private final PartnershipService partnershipService;
    public PartnershipUserController(PartnershipService partnershipService) {
        this.partnershipService = partnershipService;
    }

    @GetMapping
    public ApiResponse<List<PartnershipResponseDto>> getAllPartnerships() {
        List<PartnershipResponseDto> partnerships = partnershipService.getAllPartnerships();
        return new ApiResponse<>(1, "모든 제휴 백과 목록 조회 성공", partnerships);
    }

    @GetMapping("/{id}")
    public ApiResponse<PartnershipResponseDto> getPartnershipById(@PathVariable Long id) {
        partnershipService.increaseHitCount(id);
        PartnershipResponseDto partnership = partnershipService.getPartnershipById(id);
        return new ApiResponse<>(1, id + "번 게시글 조회 성공", partnership);
    }

    @PostMapping("/{id}/like")
    public ApiResponse<Void> increaseLikeCount(@PathVariable Long id) {
        partnershipService.increaseLikeCount(id);
        return new ApiResponse<>(1, id + "번 게시글 좋아요 수 증가 성공", null);
    }
}
