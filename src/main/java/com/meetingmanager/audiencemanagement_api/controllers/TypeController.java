package com.meetingmanager.audiencemanagement_api.controllers;

import com.meetingmanager.audiencemanagement_api.models.type.CreateType;
import com.meetingmanager.audiencemanagement_api.models.type.Type;
import com.meetingmanager.audiencemanagement_api.models.audience.Audience;
import com.meetingmanager.audiencemanagement_api.models.audience.CreateAudience;
import com.meetingmanager.audiencemanagement_api.models.audience.UpdateAudience;
import com.meetingmanager.audiencemanagement_api.services.TypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления типами аудиторий.
 */
@RestController
@RequestMapping("api/types")
public class TypeController {
    @Autowired
    private final TypeService typeService;

    /**
     * Конструктор для инъекции зависимости TypeService.
     *
     * @param typeService сервис для управления типами аудиторий
     */
    public TypeController(TypeService typeService){
        this.typeService = typeService;
    }

    /**
     * Получить все типы аудиторий.
     *
     * @return список всех типов аудиторий или статус BAD_REQUEST, если запрос не удался
     */
    @GetMapping()
    public ResponseEntity<List<Type>> getAll(){
        List<Type> types = typeService.getAll();
        if(types == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.ok(types);
    }

    /**
     * Создать новый тип аудитории.
     *
     * @param request данные для создания типа аудитории
     * @param bindingResult результат валидации
     * @return созданный тип аудитории или статус BAD_REQUEST, если данные невалидны
     */
    @PostMapping("/add")
    public ResponseEntity<Type> createType(@Valid @RequestBody CreateType request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Type createdType = typeService.create(request);
        if(createdType == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(createdType);
    }

    /**
     * Обновить тип аудитории по идентификатору.
     *
     * @param id идентификатор типа аудитории
     * @param request данные для обновления типа аудитории
     * @return обновленный тип аудитории или статус BAD_REQUEST, если обновление не удалось
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Type> updateType(@PathVariable("id") UUID id, @RequestBody CreateType request){
        Type updatedType = typeService.update(id, request);
        if(updatedType == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedType);
    }

    /**
     * Удалить тип аудитории по идентификатору.
     *
     * @param id идентификатор типа аудитории
     * @return статус OK, если тип аудитории удален, или BAD_REQUEST, если удаление не удалось
     */
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTypeById(@PathVariable UUID id) {
        if(typeService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.badRequest().build();
    }
}
