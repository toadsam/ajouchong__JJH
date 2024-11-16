package com.ajouchong.controller.admin;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.RulePostRequestDto;
import com.ajouchong.dto.response.RulePostResponseDto;
import com.ajouchong.service.RulePostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/data")
public class RulePostAdminController {
    private final RulePostService rulePostService;

    public RulePostAdminController(RulePostService rulePostService) {
        this.rulePostService = rulePostService;
    }

    @PostMapping
    public ApiResponse<RulePostResponseDto> createRulePost(@RequestBody RulePostRequestDto requestDto) {
        RulePostResponseDto responseDto = rulePostService.createRulePost(requestDto, requestDto.getRuleType());
        return new ApiResponse<>(1, responseDto.getRuleType() + " 타입의 게시글 업로드에 성공했습니다.", responseDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRulePost(@PathVariable("id") Long id) {
        rulePostService.deleteRulePost(id);
        return new ApiResponse<>(1, id +"번 게시글 삭제에 성공했습니다.", null);
    }
}
