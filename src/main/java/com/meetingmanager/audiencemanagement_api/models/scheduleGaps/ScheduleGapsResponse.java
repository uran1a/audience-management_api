package com.meetingmanager.audiencemanagement_api.models.scheduleGaps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScheduleGapsResponse {
    private UUID audience_id;
    private List<Gap> gaps;
}
