package com.meetingmanager.audiencemanagement_api.controllers;

import com.meetingmanager.audiencemanagement_api.controllers.ScheduleController;
import com.meetingmanager.audiencemanagement_api.models.freeAudience.FreeAudienceRequest;
import com.meetingmanager.audiencemanagement_api.models.freeAudience.FreeAudienceResponse;
import com.meetingmanager.audiencemanagement_api.models.schedule.Schedule;
import com.meetingmanager.audiencemanagement_api.models.scheduleGaps.ScheduleGapsRequest;
import com.meetingmanager.audiencemanagement_api.models.scheduleGaps.ScheduleGapsResponse;
import com.meetingmanager.audiencemanagement_api.services.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ScheduleControllerTests {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        schedules.add(new Schedule());
        schedules.add(new Schedule());

        when(scheduleService.getAll()).thenReturn(schedules);

        ResponseEntity<List<Schedule>> responseEntity = scheduleController.getAllSchedules();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(schedules, responseEntity.getBody());
    }

    @Test
    public void testGetScheduleGapsByAudienceId() {
        UUID id = UUID.randomUUID();
        ScheduleGapsRequest request = new ScheduleGapsRequest();
        ScheduleGapsResponse scheduleGaps = new ScheduleGapsResponse();

        when(scheduleService.getScheduleGaps(id, request.getDate())).thenReturn(scheduleGaps);

        ResponseEntity<ScheduleGapsResponse> responseEntity = scheduleController.getScheduleGapsByAudienceId(id, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(scheduleGaps, responseEntity.getBody());
    }

    @Test
    public void testGetFreeAudiences() {
        FreeAudienceRequest request = new FreeAudienceRequest();
        request.setStart_free_time(LocalDateTime.parse("2024-05-09T10:00:00"));
        List<FreeAudienceResponse> freeAudiences = new ArrayList<>();
        freeAudiences.add(new FreeAudienceResponse());

        when(scheduleService.getFreeAudiences(request)).thenReturn(freeAudiences);

        ResponseEntity<List<FreeAudienceResponse>> responseEntity = scheduleController.getFreeAudiences(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(freeAudiences, responseEntity.getBody());
    }

    @Test
    public void testGetScheduleById() {
        UUID id = UUID.randomUUID();
        Schedule schedule = new Schedule();

        when(scheduleService.getById(id)).thenReturn(schedule);

        ResponseEntity<Schedule> responseEntity = scheduleController.getScheduleById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(schedule, responseEntity.getBody());
    }

    @Test
    public void testCreateSchedule() {
        Schedule request = new Schedule();
        Schedule createdSchedule = new Schedule();

        when(scheduleService.create(request)).thenReturn(createdSchedule);

        ResponseEntity<Schedule> responseEntity = scheduleController.createSchedule(request, mock(BindingResult.class));

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdSchedule, responseEntity.getBody());
    }

    @Test
    public void testDeleteScheduleById() {
        UUID id = UUID.randomUUID();

        when(scheduleService.delete(id)).thenReturn(true);

        ResponseEntity<Void> responseEntity = scheduleController.deleteScheduleById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateSchedule() {
        UUID id = UUID.randomUUID();
        Schedule request = new Schedule();
        Schedule updatedSchedule = new Schedule();

        when(scheduleService.update(id, request)).thenReturn(updatedSchedule);

        ResponseEntity<Schedule> responseEntity = scheduleController.updateSchedule(id, request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(updatedSchedule, responseEntity.getBody());
    }
}
