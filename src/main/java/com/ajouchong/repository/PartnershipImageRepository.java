package com.ajouchong.repository;

import com.ajouchong.entity.PartnershipImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnershipImageRepository extends JpaRepository<PartnershipImage, Long> {
}
