package com.example.economy_scheduler.repository;

import com.example.economy_scheduler.entity.EconomyVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EconomyVideoRepository extends JpaRepository<EconomyVideo, Long> {
}
