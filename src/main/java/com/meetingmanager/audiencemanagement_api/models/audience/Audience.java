package com.meetingmanager.audiencemanagement_api.models.audience;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "audiences")
public class Audience {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "building")
    private int building;

    @Column(name = "floor")
    private int floor;

    @Column(name = "room")
    private int room;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "type_id")
    private UUID type_id;
}
