package com.meetingmanager.audiencemanagement_api.controllers;

import com.meetingmanager.audiencemanagement_api.controllers.AudienceController;
import com.meetingmanager.audiencemanagement_api.models.audience.Audience;
import com.meetingmanager.audiencemanagement_api.models.audience.AudienceResponse;
import com.meetingmanager.audiencemanagement_api.models.audience.CreateAudience;
import com.meetingmanager.audiencemanagement_api.models.audience.UpdateAudience;
import com.meetingmanager.audiencemanagement_api.services.AudienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AudienceControllerTests {

    @Mock
    private AudienceService audienceService;

    @InjectMocks
    private AudienceController audienceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllAudience() {
        List<AudienceResponse> audiences = new ArrayList<>();
        audiences.add(new AudienceResponse(UUID.randomUUID(), 1, 1, 101, 50, "Лекция"));
        audiences.add(new AudienceResponse(UUID.randomUUID(), 2, 3, 305, 30, "Семинар"));

        when(audienceService.getAll()).thenReturn(audiences);

        ResponseEntity<List<AudienceResponse>> responseEntity = audienceController.getAllAudience();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(audiences, responseEntity.getBody());
    }

    @Test
    public void testGetAudienceById() {
        UUID id = UUID.randomUUID();
        AudienceResponse audience = new AudienceResponse(id, 1, 2, 202, 40, "Конференция");

        when(audienceService.getById(id)).thenReturn(audience);

        ResponseEntity<AudienceResponse> responseEntity = audienceController.getAudienceById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(audience, responseEntity.getBody());
    }

    @Test
    public void testCreateAudience() {
        CreateAudience request = new CreateAudience(1, 2, 202, 40, UUID.randomUUID());
        Audience createdAudience = new Audience(UUID.randomUUID(), 1, 2, 202, 40, request.getType_id());

        when(audienceService.create(request)).thenReturn(createdAudience);

        ResponseEntity<Audience> responseEntity = audienceController.createAudience(request, mock(BindingResult.class));

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdAudience, responseEntity.getBody());
    }

    @Test
    public void testDeleteAudienceById() {
        UUID id = UUID.randomUUID();

        when(audienceService.delete(id)).thenReturn(true);

        ResponseEntity<Void> responseEntity = audienceController.deleteAudienceById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateAudience() {
        UUID id = UUID.randomUUID();
        UpdateAudience request = new UpdateAudience(1, 2, 202, 45, UUID.randomUUID());
        Audience updatedAudience = new Audience(id, 1, 2, 202, 45, request.getType_id());

        when(audienceService.update(id, request)).thenReturn(updatedAudience);

        ResponseEntity<Audience> responseEntity = audienceController.updateAudience(id, request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(updatedAudience, responseEntity.getBody());
    }
}


