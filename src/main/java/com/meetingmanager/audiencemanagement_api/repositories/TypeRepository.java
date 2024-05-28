package com.meetingmanager.audiencemanagement_api.repositories;

import com.meetingmanager.audiencemanagement_api.models.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TypeRepository extends JpaRepository<Type, UUID> {
}
