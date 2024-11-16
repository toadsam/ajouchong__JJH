package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.AgoraRequestDto;
import com.ajouchong.dto.response.AgoraResponseDto;
import com.ajouchong.entity.Agora;
import com.ajouchong.service.AgoraService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agora")
public class AgoraUserController {
    private final AgoraService agoraService;

    public AgoraUserController(AgoraService agoraService) {
        this.agoraService = agoraService;
    }

    @PostMapping
    public ApiResponse<AgoraResponseDto> createPost(@RequestBody AgoraRequestDto requestDto) {
        AgoraResponseDto responseDto = agoraService.createPost(requestDto);
        return new ApiResponse<>(1, "게시글이 게시되었습니다.", responseDto);
    }

    @GetMapping
    public ApiResponse<List<Agora>> getAllPosts() {
        List<Agora> allPosts = agoraService.getAllPosts();
        return new ApiResponse<>(1, "전체 게시글 목록 조회 성공", allPosts);
    }

    @GetMapping("{postId}")
    public ApiResponse<Optional<Agora>> getPostById(@PathVariable Long postId) {
        Optional<Agora> agora = Optional.ofNullable(agoraService.getPostById(postId));
        if (agora.isPresent()) {
            agoraService.incrementHitCount(postId);
            return new ApiResponse<>(1, postId + "번 게시글 조회 성공", agora);
        } else {
            return new ApiResponse<>(0, "게시글을 찾을 수 없습니다.", null);
        }
    }

    @PostMapping("/{postId}/like")
    public ApiResponse<Void> incrementLikeCount(@PathVariable Long postId) {
        agoraService.incrementUserLikeCount(postId);
        return new ApiResponse<>(1, postId + "번 게시글 좋아요 성공", null);
    }
}
