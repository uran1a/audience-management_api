package com.meetingmanager.audiencemanagement_api.models.audience;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateAudience {
    private int building;

    private int floor;

    private int room;

    private int capacity;
    private UUID type_id;
}
