package com.meetingmanager.audiencemanagement_api.controllers;

import com.meetingmanager.audiencemanagement_api.controllers.TypeController;
import com.meetingmanager.audiencemanagement_api.models.type.CreateType;
import com.meetingmanager.audiencemanagement_api.models.type.Type;
import com.meetingmanager.audiencemanagement_api.services.TypeService;
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

public class TypeControllerTests {

    @Mock
    private TypeService typeService;

    @InjectMocks
    private TypeController typeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() {
        List<Type> types = new ArrayList<>();
        types.add(new Type());
        types.add(new Type());

        when(typeService.getAll()).thenReturn(types);

        ResponseEntity<List<Type>> responseEntity = typeController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(types, responseEntity.getBody());
    }

    @Test
    public void testCreateType() {
        CreateType request = new CreateType();
        Type createdType = new Type();

        when(typeService.create(request)).thenReturn(createdType);

        ResponseEntity<Type> responseEntity = typeController.createType(request, mock(BindingResult.class));

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdType, responseEntity.getBody());
    }

    @Test
    public void testUpdateType() {
        UUID id = UUID.randomUUID();
        CreateType request = new CreateType();
        Type updatedType = new Type();

        when(typeService.update(id, request)).thenReturn(updatedType);

        ResponseEntity<Type> responseEntity = typeController.updateType(id, request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(updatedType, responseEntity.getBody());
    }

    @Test
    public void testDeleteTypeById() {
        UUID id = UUID.randomUUID();

        when(typeService.delete(id)).thenReturn(true);

        ResponseEntity<Void> responseEntity = typeController.deleteTypeById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}