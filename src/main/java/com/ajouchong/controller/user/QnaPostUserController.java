package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.QnaPostRequestDto;
import com.ajouchong.dto.response.QnaPostResponseDto;
import com.ajouchong.service.QnaPostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/qna")
public class QnaPostUserController {
    private final QnaPostService qnaPostService;

    public QnaPostUserController(QnaPostService qnaPostService) {
        this.qnaPostService = qnaPostService;
    }

    @PostMapping
    public ApiResponse<QnaPostResponseDto> createPost(@RequestBody QnaPostRequestDto requestDto) {
        QnaPostResponseDto responseDto = qnaPostService.createPost(requestDto);
        return new ApiResponse<>(1, "게시글이 게시되었습니다.", responseDto);
    }

    @GetMapping
    public ApiResponse<List<QnaPostResponseDto>> getAllPosts() {
        List<QnaPostResponseDto> allPosts = qnaPostService.getAllPosts();
        return new ApiResponse<>(1, "전체 게시글 목록 조회 성공", allPosts);
    }

    @GetMapping("{postId}")
    public ApiResponse<QnaPostResponseDto> getPostById(@PathVariable Long postId) {
        QnaPostResponseDto responseDto = qnaPostService.getPostById(postId);
        return new ApiResponse<>(1, postId +"번 게시글 반환에 성공했습니다.", responseDto);
    }


    @PostMapping("/{postId}/like")
    public ApiResponse<Void> incrementLikeCount(@PathVariable Long postId) {
        qnaPostService.incrementUserLikeCount(postId);
        return new ApiResponse<>(1, postId + "번 게시글 좋아요 성공", null);
    }
}
