package com.ajouchong.service;

import com.ajouchong.dto.request.NoticePostRequestDto;
import com.ajouchong.dto.response.NoticePostResponseDto;
import com.ajouchong.entity.Attachment;
import com.ajouchong.entity.Member;
import com.ajouchong.entity.NoticePost;
import com.ajouchong.entity.enumClass.AttachmentType;
import com.ajouchong.jwt.JwtTokenProvider;
import com.ajouchong.repository.MemberRepository;
import com.ajouchong.repository.NoticePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticePostService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NoticePostRepository noticePostRepository;
    private final AttachmentService attachmentService;

    @Transactional
    public NoticePostResponseDto saveNoticePost(NoticePostRequestDto requestDto, String token) throws IOException {
        String email = jwtTokenProvider.getUserEmailFromToken(token);
        Member author = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        NoticePost noticePost = new NoticePost();
        noticePost.setAuthor(author);
        noticePost.setNpTitle(requestDto.getTitle());
        noticePost.setNpContent(requestDto.getContent());

        NoticePost savedNoticePost = noticePostRepository.save(noticePost);

        List<Attachment> attachments = attachmentService.saveAttachments(requestDto.getAttachmentFiles());
        attachments.forEach(attachment -> attachment.setNoticePost(savedNoticePost));

        savedNoticePost.setAttachments(attachments);
        noticePostRepository.save(savedNoticePost);

        List<String> imageUrls = attachments.stream()
                .filter(attachment -> attachment.getAttachmentType() == AttachmentType.IMAGE)
                .map(Attachment::getStoreFilename)
                .collect(Collectors.toList());

        List<String> generalUrls = attachments.stream()
                .filter(attachment -> attachment.getAttachmentType() == AttachmentType.GENERAL)
                .map(Attachment::getStoreFilename)
                .collect(Collectors.toList());

        return NoticePostResponseDto.builder()
                .nPost_id(savedNoticePost.getNPostId())
                .npTitle(savedNoticePost.getNpTitle())
                .npContent(savedNoticePost.getNpContent())
                .npUserLikeCnt(savedNoticePost.getNpUserLikeCnt())
                .npHitCnt(savedNoticePost.getNpHitCnt())
                .npCreateTime(savedNoticePost.getNpCreateTime())
                .npUpdateTime(savedNoticePost.getNpUpdateTime())
                .imageUrls(imageUrls)
                .generalUrls(generalUrls)
                .build();
    }

    @Transactional(readOnly = true)
    public List<NoticePostResponseDto> getAllNoticePosts() {
        List<NoticePost> noticePosts = noticePostRepository.findAll();

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

    private NoticePostResponseDto convertToResponseDto(NoticePost noticePost) {
        List<String> imageUrls = noticePost.getAttachments().stream()
                .filter(attachment -> attachment.getAttachmentType() == AttachmentType.IMAGE)
                .map(Attachment::getStoreFilename)
                .collect(Collectors.toList());

        List<String> generalUrls = noticePost.getAttachments().stream()
                .filter(attachment -> attachment.getAttachmentType() == AttachmentType.GENERAL)
                .map(Attachment::getStoreFilename)
                .collect(Collectors.toList());

        return NoticePostResponseDto.builder()
                .nPost_id(noticePost.getNPostId())
                .npTitle(noticePost.getNpTitle())
                .npContent(noticePost.getNpContent())
                .npUserLikeCnt(noticePost.getNpUserLikeCnt())
                .npHitCnt(noticePost.getNpHitCnt())
                .npCreateTime(noticePost.getNpCreateTime())
                .npUpdateTime(noticePost.getNpUpdateTime())
                .imageUrls(imageUrls)
                .generalUrls(generalUrls)
                .build();
    }
}

