package com.meetingmanager.audiencemanagement_api.controllers;

import com.meetingmanager.audiencemanagement_api.models.freeAudience.FreeAudienceRequest;
import com.meetingmanager.audiencemanagement_api.models.freeAudience.FreeAudienceResponse;
import com.meetingmanager.audiencemanagement_api.models.schedule.Schedule;
import com.meetingmanager.audiencemanagement_api.models.scheduleGaps.ScheduleGapsRequest;
import com.meetingmanager.audiencemanagement_api.models.scheduleGaps.ScheduleGapsResponse;
import com.meetingmanager.audiencemanagement_api.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления расписаниями.
 */
@RestController
@RequestMapping("api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    /**
     * Конструктор для инъекции зависимости ScheduleService.
     *
     * @param scheduleService сервис для управления расписаниями
     */
    @Autowired
    public ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    /**
     * Получить все расписания.
     *
     * @return список всех расписаний или статус BAD_REQUEST, если запрос не удался
     */
    @GetMapping()
    public ResponseEntity<List<Schedule>> getAllSchedules(){
        var audiences = scheduleService.getAll();
        if(audiences == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.ok(audiences);
    }

    /**
     * Получить свободные промежутки времени для аудитории по идентификатору.
     *
     * @param id идентификатор аудитории
     * @param request запрос с датой для поиска свободных промежутков
     * @return свободные промежутки времени или статус BAD_REQUEST, если запрос не удался
     */
    @PostMapping("/gaps/{id}")
    public ResponseEntity<ScheduleGapsResponse> getScheduleGapsByAudienceId(@PathVariable("id") UUID id, @RequestBody ScheduleGapsRequest request){
        var scheduleGaps = scheduleService.getScheduleGaps(id, request.getDate());
        return ResponseEntity.ok(scheduleGaps);
    }

    /**
     * Получить свободные аудитории на заданное время.
     *
     * @param time запрос с началом и концом времени
     * @return список свободных аудиторий или статус BAD_REQUEST, если запрос не удался
     */
    @PostMapping("/free")
    public ResponseEntity<List<FreeAudienceResponse>> getFreeAudiences(@RequestBody FreeAudienceRequest time){
        if(time.getStart_free_time() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        var freeAudience = scheduleService.getFreeAudiences(time);

        return ResponseEntity.ok(freeAudience);
    }

    /**
     * Получить расписание по идентификатору.
     *
     * @param id идентификатор расписания
     * @return расписание или статус BAD_REQUEST, если расписание не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable UUID id){
        Schedule schedule = scheduleService.getById(id);
        if(schedule == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.ok(schedule);
    }

    /**
     * Получить расписание для аудитории по идентификатору.
     *
     * @param id идентификатор аудитории
     * @return список расписаний для аудитории или статус BAD_REQUEST, если запрос не удался
     */
    @GetMapping("/audience/{id}")
    public ResponseEntity<List<Schedule>> getScheduleByAudienceId(@PathVariable UUID id){
        List<Schedule> schedules = scheduleService.getByAudienceId(id);
        if(schedules == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.ok(schedules);
    }

    /**
     * Создать новое расписание.
     *
     * @param request данные для создания расписания
     * @param bindingResult результат валидации
     * @return созданное расписание или статус BAD_REQUEST, если данные невалидны
     */
    @PostMapping("/add")
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody Schedule request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        var createdSchedule = scheduleService.create(request);
        if(createdSchedule == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
    }

    /**
     * Удалить расписание по идентификатору.
     *
     * @param id идентификатор расписания
     * @return статус OK, если расписание удалено, или BAD_REQUEST, если удаление не удалось
     */
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteScheduleById(@PathVariable UUID id) {
        if(scheduleService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.badRequest().build();
    }

    /**
     * Обновить расписание по идентификатору.
     *
     * @param id идентификатор расписания
     * @param request данные для обновления расписания
     * @return обновленное расписание или статус BAD_REQUEST, если обновление не удалось
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable("id") UUID id, @RequestBody Schedule request){
        Schedule updatedSchedule = scheduleService.update(id, request);
        if(updatedSchedule == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedSchedule);
    }
}
