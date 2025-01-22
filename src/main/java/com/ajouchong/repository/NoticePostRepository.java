package com.ajouchong.repository;

import com.ajouchong.entity.NoticePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticePostRepository extends JpaRepository<NoticePost, Long> {
    Page<NoticePost> findAllByOrderByNpCreateTimeDesc(Pageable pageable);
}
