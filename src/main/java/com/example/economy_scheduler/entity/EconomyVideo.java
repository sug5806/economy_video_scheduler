package com.example.economy_scheduler.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class EconomyVideo {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String url;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_channel_id", foreignKey = @ForeignKey(name = "FK_EconomyVideo_ScheduleChannel"))
    private ScheduleChannel scheduleChannel;

    public void mappingChannel(ScheduleChannel channel) {
        channel.getEconomyVideoList().add(this);
        this.scheduleChannel = channel;
    }
}
