package com.ajouchong.controller.admin;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.PartnershipRequestDto;
import com.ajouchong.dto.response.PartnershipResponseDto;
import com.ajouchong.service.PartnershipService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/partnership")
public class PartnershipAdminController {
    private final PartnershipService partnershipService;

    public PartnershipAdminController(PartnershipService partnershipService) {
        this.partnershipService = partnershipService;
    }

    @PostMapping
    public ApiResponse<PartnershipResponseDto> uploadPartnership(@RequestBody PartnershipRequestDto requestDto) {
        PartnershipResponseDto savedPartner = partnershipService.savePartnership(requestDto);
        return new ApiResponse<>(1, "제휴 백과 업로드 성공", savedPartner);
    }

    @PutMapping("/{id}")
    public ApiResponse<PartnershipResponseDto> changePartnership(@PathVariable Long id, @RequestBody PartnershipRequestDto requestDto) {
        PartnershipResponseDto updatedPartner = partnershipService.changePartnership(id, requestDto);
        return new ApiResponse<>(1, id + "번 게시글 수정 성공", updatedPartner);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePartnership(@PathVariable Long id) {
        partnershipService.deletePartnership(id);
        return new ApiResponse<>(1, id + "번 게시글 삭제 성공", null);
    }

}
