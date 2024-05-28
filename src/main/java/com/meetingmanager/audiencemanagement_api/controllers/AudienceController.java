package com.meetingmanager.audiencemanagement_api.controllers;

import com.meetingmanager.audiencemanagement_api.models.audience.Audience;
import com.meetingmanager.audiencemanagement_api.models.audience.AudienceResponse;
import com.meetingmanager.audiencemanagement_api.models.audience.CreateAudience;
import com.meetingmanager.audiencemanagement_api.models.audience.UpdateAudience;
import com.meetingmanager.audiencemanagement_api.services.AudienceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления аудиториями.
 */
@RestController
@RequestMapping("api/audiences")
public class AudienceController {
    private final AudienceService audienceService;

    /**
     * Конструктор для инъекции зависимости AudienceService.
     *
     * @param audienceService сервис для управления аудиториями
     */
    @Autowired
    public AudienceController(AudienceService audienceService){
        this.audienceService = audienceService;
    }

    /**
     * Получить все аудитории.
     *
     * @return список всех аудиторий или статус BAD_REQUEST, если запрос не удался
     */
    @GetMapping()
    public ResponseEntity<List<AudienceResponse>> getAllAudience(){
        var audiences = audienceService.getAll();
        if(audiences == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.ok(audiences);
    }

    /**
     * Получить аудиторию по идентификатору.
     *
     * @param id идентификатор аудитории
     * @return аудитория или статус BAD_REQUEST, если аудитория не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<AudienceResponse> getAudienceById(@PathVariable UUID id){
        var audience = audienceService.getById(id);
        if(audience == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.ok(audience);
    }

    /**
     * Создать новую аудиторию.
     *
     * @param request        данные для создания аудитории
     * @param bindingResult  результат валидации
     * @return созданная аудитория или статус BAD_REQUEST, если данные невалидны
     */
    @PostMapping("/add")
    public ResponseEntity<Audience> createAudience(@Valid @RequestBody CreateAudience request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Audience createdAudience = audienceService.create(request);
        if(createdAudience == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAudience);
    }

    /**
     * Удалить аудиторию по идентификатору.
     *
     * @param id идентификатор аудитории
     * @return статус OK, если аудитория удалена, или BAD_REQUEST, если удаление не удалось
     */
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAudienceById(@PathVariable UUID id) {
        if(audienceService.delete(id))
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    /**
     * Обновить аудиторию по идентификатору.
     *
     * @param id идентификатор аудитории
     * @param request данные для обновления аудитории
     * @return обновленная аудитория или статус BAD_REQUEST, если обновление не удалось
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Audience> updateAudience(@PathVariable("id") UUID id, @RequestBody UpdateAudience request){
        var updatedAudience = audienceService.update(id, request);
        if(updatedAudience == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedAudience);
    }
}
