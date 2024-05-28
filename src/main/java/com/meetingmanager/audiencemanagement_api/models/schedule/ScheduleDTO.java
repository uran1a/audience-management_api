package com.meetingmanager.audiencemanagement_api.models.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScheduleDTO {
    @Null
    private UUID id;
    @Null
    private UUID audience_id;
    @Null
    private UUID meeting_id;
    @Null
    private LocalDateTime start_time;
    @Null
    private LocalDateTime end_time;
}
