package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.response.NoticePostResponseDto;
import com.ajouchong.service.NoticePostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notice")
public class NoticePostUserController {
    private final NoticePostService noticePostService;

    public NoticePostUserController(NoticePostService noticePostService) {
        this.noticePostService = noticePostService;
    }

    @GetMapping
    public ApiResponse<List<NoticePostResponseDto>> getAllNoticePosts() {
        List<NoticePostResponseDto> posts = noticePostService.getAllNoticePosts();
        return new ApiResponse<>(1, "모든 게시글 조회 성공", posts);
    }

    // 특정 게시물 조회
    @GetMapping("/{id}")
    public ApiResponse<NoticePostResponseDto> getNoticePostById(@PathVariable Long id) {
        noticePostService.increaseHitCount(id);
        NoticePostResponseDto post = noticePostService.getNoticePostById(id);
        return new ApiResponse<>(1, id + "번 게시글 조회 성공", post);
    }

    @PostMapping("/{id}/like")
    public ApiResponse<Void> increaseLikeCount(@PathVariable Long id) {
        noticePostService.increaseLikeCount(id);
        return new ApiResponse<>(1, id + "번 게시글 좋아요 수 증가 성공", null);
    }
}
