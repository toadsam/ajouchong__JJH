package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.entity.Admin;
import com.ajouchong.entity.IntroPost;
import com.ajouchong.entity.enumClass.IntroPostPageName;
import com.ajouchong.service.AdminService;
import com.ajouchong.service.IntroPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/about")
public class IntroPostUserController {

    private final IntroPostService introPostService;
    private final AdminService adminService;

    @GetMapping("/{page}")
    public ApiResponse<IntroPost> getPhotoUrl(@PathVariable("page") String pageName) {
        try {
            IntroPostPageName page = IntroPostPageName.valueOf(pageName.toUpperCase());
            Optional<IntroPost> postOptional = introPostService.getPhotosByPage(page);
            return postOptional.map(introPost -> new ApiResponse<>(1, "Post 객체 반환 성공", introPost))
                    .orElseGet(() -> new ApiResponse<>(0, "해당 페이지의 포스트가 존재하지 않습니다", null));
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(0, "유효하지 않은 페이지 이름입니다: " + pageName, null);
        }
    }

    @GetMapping("/information")
    public ApiResponse<Admin> getAdminInfo() {
        Optional<Admin> admin = adminService.getAdminContactInfo();

        return admin.map(value -> new ApiResponse<>(1, "관리자 정보 반환 성공", value)).orElseGet(()
                -> new ApiResponse<>(0, "관리자 정보가 없습니다", null));
    }
}
