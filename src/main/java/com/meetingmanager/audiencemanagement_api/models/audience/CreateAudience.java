package com.meetingmanager.audiencemanagement_api.models.audience;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateAudience {
    private int building;

    private int floor;

    private int room;

    private int capacity;
    private UUID type_id;
}
