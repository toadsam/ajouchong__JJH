package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.response.NoticePostResponseDto;
import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.service.NoticePostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/notice")
public class NoticePostUserController {
    private final NoticePostService noticePostService;
    private final JwtTokenProvider jwtTokenProvider;

    public NoticePostUserController(NoticePostService noticePostService, JwtTokenProvider jwtTokenProvider) {
        this.noticePostService = noticePostService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ApiResponse<List<NoticePostResponseDto>> getAllNoticePosts() {
        List<NoticePostResponseDto> allPosts = noticePostService.getLatestNoticePosts();
        return new ApiResponse<>(1, "모든 게시글 조회 성공", allPosts);
    }

    // 특정 게시물 조회
    @GetMapping("/{id}")
    public ApiResponse<NoticePostResponseDto> getNoticePostById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token != null) {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // "Bearer " 제거
            } else {
                throw new JwtTokenProvider.InvalidJwtException("유효하지 않은 Authorization 헤더 형식입니다.");
            }
        }

        NoticePostResponseDto post = noticePostService.getNoticePostWithHitIncrement(id, token);

        return new ApiResponse<>(1, id + "번 게시글 조회 성공", post);
    }


    @PostMapping("/{id}/like")
    public ApiResponse<Boolean> toggleLike(@PathVariable Long id,
                                           @CookieValue(value = "accessToken", required = false) String token) {

        if (token == null) {
            return new ApiResponse<>(0, "로그인이 필요합니다.", null);
        }

        if (!jwtTokenProvider.validateToken(token)) {
            return new ApiResponse<>(0, "유효하지 않은 JWT 토큰입니다.", null);
        }

        boolean isLike = noticePostService.toggleLike(id, token);
        String message = isLike ? "번 게시글 좋아요 성공" : "번 게시글 좋아요 취소 성공";

        return new ApiResponse<>(1, id + message, isLike);
    }

}
