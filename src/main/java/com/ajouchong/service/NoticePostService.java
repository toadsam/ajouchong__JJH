package com.ajouchong.service;

import com.ajouchong.dto.request.NoticePostRequestDto;
import com.ajouchong.dto.response.NoticePostResponseDto;
import com.ajouchong.entity.Member;
import com.ajouchong.entity.NoticePost;
import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.repository.MemberRepository;
import com.ajouchong.repository.NoticePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticePostService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NoticePostRepository noticePostRepository;
    private final S3UploadService s3UploadService;

    /*
    login 후 게시글 업로드
    @Transactional
    public NoticePostResponseDto saveNoticePost(NoticePostRequestDto requestDto, String token) throws IOException {
        // 토큰에서 loginId 추출
        String loginId = jwtTokenProvider.getLoginId(token);

        // loginId로 Member 조회
        Member author = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 게시물 저장
        NoticePost noticePost = NoticePost.builder()
                .author(author) // Member 객체
                .npTitle(requestDto.getTitle())
                .npContent(requestDto.getContent())
                .build();


        NoticePost savedNoticePost = noticePostRepository.save(noticePost);

        // 첨부 파일 처리
        List<Attachment> attachments = attachmentService.saveAttachments(requestDto.getAttachmentFiles());
        attachments.forEach(attachment -> attachment.setNoticePost(savedNoticePost));

        savedNoticePost.setAttachments(attachments);
        noticePostRepository.save(savedNoticePost);

        return convertToResponseDto(savedNoticePost);
    }
     */

    @Transactional
    public NoticePostResponseDto saveNoticePost(NoticePostRequestDto requestDto, String token) throws IOException {
        Member author = null;

        // 로그인된 사용자 정보 추출 (토큰이 있을 경우에만)
        if (token != null && !token.isBlank()) {
            try {
                String loginId = jwtTokenProvider.getLoginId(token);
                author = memberRepository.findByLoginId(loginId)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            } catch (Exception e) {
                // 토큰이 유효하지 않을 경우 무시하고 비로그인 상태로 처리
                author = null;
            }
        }

        // 이미지 파일 처리
        List<String> imageUrls = new ArrayList<>();
        if (requestDto.getImageFiles() != null && !requestDto.getImageFiles().isEmpty()) {
            for (MultipartFile file : requestDto.getImageFiles()) {
                String image = s3UploadService.saveFile(file); // S3에 업로드 후 URL 반환
                imageUrls.add(image);
            }
        }

        NoticePost noticePost = requestDto.createNoticePost(imageUrls);
        noticePost.setImageUrls(imageUrls);
        noticePost.setAuthor(author);

        NoticePost savedNoticePost = noticePostRepository.save(noticePost);

        return convertToResponseDto(savedNoticePost);
    }

    public List<NoticePostResponseDto> getLatestNoticePosts() {
        List<NoticePost> noticePosts = noticePostRepository.findAll(Sort.by(Sort.Direction.DESC, "npCreateTime"));

        return noticePosts.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NoticePostResponseDto getNoticePostById(Long id) {
        NoticePost post = noticePostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시글을 찾을 수 없습니다."));
        return convertToResponseDto(post);
    }

    @Transactional
    public void deleteNoticePost(Long id) {
        if (!noticePostRepository.existsById(id)) {
            throw new RuntimeException(id + "번 게시글을 찾을 수 없습니다.");
        }
        noticePostRepository.deleteById(id);
    }

    @Transactional
    public void increaseLikeCount(Long id) {
        NoticePost noticePost = noticePostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시글을 찾을 수 없습니다."));
        noticePost.setNpUserLikeCnt(noticePost.getNpUserLikeCnt() + 1);
        noticePostRepository.save(noticePost);
    }

    @Transactional
    public void increaseHitCount(Long id) {
        NoticePost noticePost = noticePostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시글을 찾을 수 없습니다."));
        noticePost.setNpHitCnt(noticePost.getNpHitCnt() + 1);
        noticePostRepository.save(noticePost);
    }

    public NoticePostResponseDto convertToResponseDto(NoticePost noticePost) {
        return new NoticePostResponseDto(noticePost);
    }

}

