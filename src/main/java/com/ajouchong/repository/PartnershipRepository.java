package com.ajouchong.repository;

import com.ajouchong.entity.Partnership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnershipRepository extends JpaRepository<Partnership, Long> {
    Page<Partnership> findAllByOrderByPsCreateTimeDesc(Pageable pageable);
}
