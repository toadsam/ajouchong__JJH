package com.ajouchong.controller.admin;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.AdminInfoRequestDto;
import com.ajouchong.entity.Admin;
import com.ajouchong.entity.IntroPost;
import com.ajouchong.entity.enumClass.IntroPostPageName;
import com.ajouchong.service.AdminService;
import com.ajouchong.service.IntroPostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/about")
public class IntroPostAdminController {
    private final IntroPostService introPostService;
    private final AdminService adminService;

    public IntroPostAdminController(IntroPostService introPostService, AdminService adminService) {
        this.introPostService = introPostService;
        this.adminService = adminService;
    }

    @PostMapping("/{page}")
    public ApiResponse<IntroPost> uploadPhoto(@PathVariable("page") String pageName, @RequestParam String imageUrl) {
        try {
            IntroPostPageName page = IntroPostPageName.valueOf(pageName.toUpperCase());
            IntroPost savedPost = introPostService.uploadImage(page, imageUrl);
            return new ApiResponse<>(1, "사진 업로드를 완료했습니다.", savedPost);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(0, "유효하지 않은 페이지 이름입니다: " + pageName, null);
        }
    }

    @PostMapping("/information")
    public ApiResponse<Admin> uploadAdminInfo(@RequestBody AdminInfoRequestDto adminInfo) {
        Admin admin = adminService.saveAdminContactInfo(adminInfo);
        return new ApiResponse<>(1, "관리자 정보 업로드 완료", admin);
    }
}
