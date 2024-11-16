package com.ajouchong.controller.admin;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.service.AgoraService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/agora")
public class AgoraAdminController {
    private final AgoraService agoraService;

    public AgoraAdminController(AgoraService agoraService) {
        this.agoraService = agoraService;
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        agoraService.deletePost(postId);
        return new ApiResponse<>(1, "게시글이 삭제되었습니다.", null);
    }

    @PutMapping("/{postId}/approve")
    public ApiResponse<Void> approvePost(@PathVariable Long postId) {
        agoraService.approvePost(postId);
        return new ApiResponse<>(1, "게시글이 승인되었습니다.", null);
    }
}