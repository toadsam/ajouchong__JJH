package com.ajouchong.service;

import com.ajouchong.dto.request.AnswerRequestDto;
import com.ajouchong.dto.request.QnaPostRequestDto;
import com.ajouchong.dto.response.AnswerResponseDto;
import com.ajouchong.dto.response.QnaPostResponseDto;
import com.ajouchong.entity.Answer;
import com.ajouchong.entity.QnaPost;
import com.ajouchong.repository.AnswerRepository;
import com.ajouchong.repository.QnaPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QnaPostService {
    private final QnaPostRepository qnaPostRepository;

    public QnaPostService(QnaPostRepository qnaPostRepository, AnswerRepository answerRepository) {
        this.qnaPostRepository = qnaPostRepository;
    }

    @Transactional
    public QnaPostResponseDto createPost(QnaPostRequestDto requestDto) {
        QnaPost post = new QnaPost();

        post.setQpTitle(requestDto.getQpTitle());
        post.setQpContent(requestDto.getQpContent());

        post.setQpUserLikeCnt(0);
        post.setQpHitCnt(0);
        post.setQpUpdateTime(LocalDateTime.now());
        post.setQpCreateTime(LocalDateTime.now());

        qnaPostRepository.save(post);

        return new QnaPostResponseDto(post);
    }


    @Transactional
    public QnaPostResponseDto getPostById(Long id) {
        QnaPost qnaPost = qnaPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "번 게시글을 찾을 수 없습니다."));
        qnaPost.incrementHitCount();
        return new QnaPostResponseDto(qnaPost);
    }

    public List<QnaPostResponseDto> getAllPosts() {
        return qnaPostRepository.findAll().stream()
                .map(QnaPostResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void incrementUserLikeCount(Long postId) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(postId + "번 게시글을 찾을 수 없습니다."));

        post.incrementUserLikeCount();
        qnaPostRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(postId + "번 게시글을 찾을 수 없습니다."));

        qnaPostRepository.delete(post);
    }

    @Transactional
    public AnswerResponseDto addAnswer(Long postId, AnswerRequestDto requestDto) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(postId + "번 게시글을 찾을 수 없습니다."));

        Answer answer = post.getAnswer();

        if (answer != null) {
            answer.setContent(requestDto.getContent());
            answer.setUpdateTime(LocalDateTime.now());
        } else {
            answer = new Answer();
            answer.setContent(requestDto.getContent());
//            answer.setQnaPost(post);
            answer.setCreateTime(LocalDateTime.now());
            answer.setUpdateTime(LocalDateTime.now());
            post.setAnswer(answer);
            post.setReplied(true);

        }

        qnaPostRepository.save(post);

        return new AnswerResponseDto(answer);
    }

    @Transactional
    public QnaPostResponseDto deleteAnswer(Long postId) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (post.getAnswer() != null) {
            post.setAnswer(null);
            post.setReplied(false);
            QnaPost updatedPost = qnaPostRepository.save(post);
            return new QnaPostResponseDto(updatedPost);
        } else {
            throw new IllegalArgumentException("해당 게시글에 답변이 없습니다.");
        }
    }
}