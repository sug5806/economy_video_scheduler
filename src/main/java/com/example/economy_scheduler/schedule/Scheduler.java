package com.example.economy_scheduler.schedule;

import com.example.economy_scheduler.entity.ScheduleChannel;
import com.example.economy_scheduler.repository.ScheduleChannelRepository;
import com.example.economy_scheduler.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private List<ScheduleChannel> URL_LIST = new ArrayList<>();
    private final ScheduleChannelRepository scheduleChannelRepository;
    private final ScheduleService scheduleService;

    @Scheduled(cron = "0 0 0/3 * * *")
    @Bean
    public void cronJob() throws ExecutionException, InterruptedException {
        URL_LIST = scheduleChannelRepository.findAll();

        log.info("크론 작업 시작 : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        for (ScheduleChannel scheduleChannel : URL_LIST) {
            scheduleService.economyVideoScheduling(scheduleChannel);
        }

    }
}
