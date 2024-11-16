package com.ajouchong.repository;

import com.ajouchong.entity.IntroPost;
import com.ajouchong.entity.enumClass.IntroPostPageName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IntroPostRepository extends JpaRepository<IntroPost, Long> {
    Optional<IntroPost> findByPage(IntroPostPageName page);
}
