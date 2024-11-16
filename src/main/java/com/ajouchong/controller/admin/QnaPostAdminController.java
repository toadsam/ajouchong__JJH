package com.ajouchong.controller.admin;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.AnswerRequestDto;
import com.ajouchong.dto.response.AnswerResponseDto;
import com.ajouchong.dto.response.QnaPostResponseDto;
import com.ajouchong.service.QnaPostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/qna")
public class QnaPostAdminController {
    private final QnaPostService qnaPostService;

    public QnaPostAdminController(QnaPostService qnaPostService) {
        this.qnaPostService = qnaPostService;
    }

    @PostMapping("/{postId}/answer")
    public ApiResponse<AnswerResponseDto> addAnswer(@PathVariable Long postId, @RequestBody AnswerRequestDto requestDto) {
        System.out.println(requestDto.getContent());
        AnswerResponseDto response = qnaPostService.addAnswer(postId, requestDto);
        return new ApiResponse<>(1, "답변이 게시/변경 되었습니다.", response);
    }

    @DeleteMapping("/{postId}/answer")
    public ApiResponse<QnaPostResponseDto> deleteAnswer(@PathVariable Long postId) {
        QnaPostResponseDto response = qnaPostService.deleteAnswer(postId);
        return new ApiResponse<>(1, "답변이 삭제되었습니다.", response);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        qnaPostService.deletePost(postId);
        return new ApiResponse<>(1, "게시글이 삭제되었습니다.", null);
    }
}
