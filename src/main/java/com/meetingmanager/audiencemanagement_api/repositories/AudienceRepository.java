package com.meetingmanager.audiencemanagement_api.repositories;

import com.meetingmanager.audiencemanagement_api.models.audience.Audience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AudienceRepository extends JpaRepository<Audience, UUID> {

}
