package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.response.RulePostResponseDto;
import com.ajouchong.entity.enumClass.RuleType;
import com.ajouchong.service.RulePostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/data")
public class RulePostUserController {
    private final RulePostService rulePostService;

    public RulePostUserController(RulePostService rulePostService) {
        this.rulePostService = rulePostService;
    }

    @GetMapping
    public ApiResponse<List<RulePostResponseDto>> getAllRulePosts(@RequestParam RuleType type) {
        List<RulePostResponseDto> responseDto;
        if (type == RuleType.OFFICIAL) {
            responseDto = rulePostService.getAllOfficialPosts();
        } else if (type == RuleType.DETAIL) {
            responseDto = rulePostService.getAllDetailPosts();
        } else {
            throw new IllegalArgumentException("유효하지 않은 RuleType 입니다.");
        }
        return new ApiResponse<>(1, type+"타입의 게시글 목록 반환 완료", responseDto);
    }

    @GetMapping("/{id}")
    public ApiResponse<RulePostResponseDto> getRulePostById(@PathVariable Long id) {
        RulePostResponseDto responseDto = rulePostService.getRulePostById(id);
        return new ApiResponse<>(1, id +"번 게시글 반환에 성공했습니다.", responseDto);
    }

    @PostMapping("/{id}/like")
    public ApiResponse<RulePostResponseDto> likeRulePost(@PathVariable Long id) {
        rulePostService.increaseLikeCount(id);
        return new ApiResponse<>(1, "번 게시글 좋아요 수 증가 성공", null);
    }
}
