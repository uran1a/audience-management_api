package com.meetingmanager.audiencemanagement_api.models.scheduleGaps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Gap {
    private LocalDateTime start;
    private LocalDateTime end;
}
