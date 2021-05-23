package com.example.economy_scheduler.repository;

import com.example.economy_scheduler.entity.ScheduleChannel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleChannelRepository extends JpaRepository<ScheduleChannel, Long> {

    @Override
    @EntityGraph(attributePaths = {"economyVideoList"})
    List<ScheduleChannel> findAll();
}
