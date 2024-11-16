package com.ajouchong.repository;

import com.ajouchong.entity.RulePost;
import com.ajouchong.entity.enumClass.RuleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RulePostRepository extends JpaRepository<RulePost, Long> {
    List<RulePost> findByType(RuleType type);
}
