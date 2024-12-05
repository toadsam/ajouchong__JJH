package com.ajouchong.controller.admin;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.NoticePostAddFormDto;
import com.ajouchong.dto.request.NoticePostRequestDto;
import com.ajouchong.dto.response.NoticePostResponseDto;
import com.ajouchong.entity.Member;
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

//    @PostMapping
//    public ApiResponse<NoticePostResponseDto> uploadNoticePost(
//            @ModelAttribute NoticePostAddFormDto requestDto,
//            @RequestHeader("Authorization") String authorizationHeader) throws IOException {
//
//        String token = authorizationHeader.substring(7);
//        NoticePostRequestDto noticePostRequestDto = requestDto.createNoticePostDto(jwtTokenProvider.getUserFromToken(token));
//        NoticePostResponseDto savedNoticePost = noticePostService.saveNoticePost(noticePostRequestDto, token);
//
//        return new ApiResponse<>(1, "게시글 업로드 성공", savedNoticePost);
//    }

    @PostMapping
    public ApiResponse<NoticePostResponseDto> uploadNoticePost(
            @ModelAttribute NoticePostAddFormDto requestDto,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) throws IOException {

        String token = null;
        Member user = null;

        // Authorization 헤더가 존재하면 토큰 처리
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                user = jwtTokenProvider.getUserFromToken(token); // 토큰에서 사용자 정보 추출
            } catch (Exception e) {
                System.out.println("유효하지 않은 토큰: " + e.getMessage());
            }
        }

        // NoticePostRequestDto 생성 (user가 null일 수도 있음)
        NoticePostRequestDto noticePostRequestDto = requestDto.createNoticePostDto(user);
        NoticePostResponseDto savedNoticePost = noticePostService.saveNoticePost(noticePostRequestDto, token);

        return new ApiResponse<>(1, "게시글 업로드 성공", savedNoticePost);
    }



    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNoticePost(@PathVariable Long id) {
        noticePostService.deleteNoticePost(id);
        return new ApiResponse<>(1, id + "번 게시글 삭제 성공", null);
    }
}
