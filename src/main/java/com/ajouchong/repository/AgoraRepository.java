package com.ajouchong.repository;

import com.ajouchong.entity.Agora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgoraRepository extends JpaRepository<Agora, Long> {
}
