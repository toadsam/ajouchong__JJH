package com.ajouchong.service;

import com.ajouchong.dto.request.AgoraRequestDto;
import com.ajouchong.dto.response.AgoraResponseDto;
import com.ajouchong.entity.Agora;
import com.ajouchong.repository.AgoraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgoraService {
    private final AgoraRepository agoraRepository;

    public AgoraService(AgoraRepository agoraRepository) {
        this.agoraRepository = agoraRepository;
    }

    @Transactional
    public AgoraResponseDto createPost(AgoraRequestDto requestDto) {
        Agora agora = new Agora();

        agora.setApTitle(requestDto.getApTitle());
        agora.setApContent(requestDto.getApContent());

        agora.setApUserLikeCount(0);
        agora.setApHitCount(0);

        agoraRepository.save(agora);

        return new AgoraResponseDto(agora);
    }

    @Transactional(readOnly = true)
    public Agora getPostById(Long postId) {
        return agoraRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(postId + "번 게시글을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Agora> getAllPosts() {
        return agoraRepository.findAll();
    }


    @Transactional
    public void deletePost(Long postId) {
        Agora agora = agoraRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(postId + "번 게시글을 찾을 수 없습니다."));

        agoraRepository.delete(agora);
    }

    @Transactional
    public void approvePost(Long postId) {
        Agora agora = getPostById(postId);
        agora.approvePost();
        agoraRepository.save(agora);
    }

    @Transactional
    public void incrementHitCount(Long postId) {
        Agora agora = agoraRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(postId + "번 게시글을 찾을 수 없습니다."));

        agora.incrementHitCount();
        agoraRepository.save(agora);
    }

    @Transactional
    public void incrementUserLikeCount(Long postId) {
        Agora agora = agoraRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(postId + "번 게시글을 찾을 수 없습니다."));

        agora.incrementUserLikeCount();
        agoraRepository.save(agora);
    }
}
