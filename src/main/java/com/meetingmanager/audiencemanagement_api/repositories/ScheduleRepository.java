package com.meetingmanager.audiencemanagement_api.repositories;

import com.meetingmanager.audiencemanagement_api.models.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
}
