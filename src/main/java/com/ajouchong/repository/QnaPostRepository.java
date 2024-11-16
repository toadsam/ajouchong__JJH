package com.ajouchong.repository;

import com.ajouchong.entity.QnaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {
}
