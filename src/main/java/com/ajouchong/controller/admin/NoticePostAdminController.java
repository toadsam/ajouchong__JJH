package com.ajouchong.controller.admin;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.NoticePostAddFormDto;
import com.ajouchong.dto.request.NoticePostRequestDto;
import com.ajouchong.dto.response.NoticePostResponseDto;
import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.service.NoticePostService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/admin/notice")
public class NoticePostAdminController {
    private final NoticePostService noticePostService;
    private final JwtTokenProvider jwtTokenProvider;

    public NoticePostAdminController(NoticePostService noticePostService, JwtTokenProvider jwtTokenProvider) {
        this.noticePostService = noticePostService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ApiResponse<NoticePostResponseDto> uploadNoticePost(
            @ModelAttribute NoticePostAddFormDto requestDto,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        String token = authorizationHeader.substring(7);
        NoticePostRequestDto noticePostRequestDto = requestDto.createNoticePostDto(jwtTokenProvider.getUserFromToken(token));
        NoticePostResponseDto savedNoticePost = noticePostService.saveNoticePost(noticePostRequestDto, token);

        return new ApiResponse<>(1, "게시글 업로드 성공", savedNoticePost);
    }


    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNoticePost(@PathVariable Long id) {
        noticePostService.deleteNoticePost(id);
        return new ApiResponse<>(1, id + "번 게시글 삭제 성공", null);
    }
}
