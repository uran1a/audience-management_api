package com.meetingmanager.audiencemanagement_api.models.freeAudience;

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
public class FreeAudienceResponse {
    private UUID id;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
}
