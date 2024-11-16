package com.ajouchong.controller.admin;

//@RestController
//@RequestMapping("api/admin/notice")
//public class NoticePostAdminController {
//    private final NoticePostService noticePostService;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    public NoticePostAdminController(NoticePostService noticePostService, JwtTokenProvider jwtTokenProvider) {
//        this.noticePostService = noticePostService;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
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
//
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteNoticePost(@PathVariable Long id) {
//        noticePostService.deleteNoticePost(id);
//        return new ApiResponse<>(1, id + "번 게시글 삭제 성공", null);
//    }
//}
