package com.meetingmanager.audiencemanagement_api.services;

import com.meetingmanager.audiencemanagement_api.models.audience.UpdateAudience;
import com.meetingmanager.audiencemanagement_api.models.schedule.Schedule;
import com.meetingmanager.audiencemanagement_api.models.type.CreateType;
import com.meetingmanager.audiencemanagement_api.models.type.Type;
import com.meetingmanager.audiencemanagement_api.repositories.AudienceRepository;
import com.meetingmanager.audiencemanagement_api.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private AudienceRepository audienceRepository;
    @Autowired
    private AudienceService audienceService;

    public boolean delete(UUID id){
        try {
            var audiences = audienceRepository.findAll().stream()
                    .filter(x -> x.getType_id().equals(id))
                    .toList();

            if(audiences.size() > 0){
                for (var audience : audiences)
                    audienceService.delete(audience.getId());
            }

            typeRepository.deleteById(id);
        }catch (Exception ex){
            System.out.println("Request error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public Type create(CreateType request) {
        var type = Type.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .build();
        return typeRepository.save(type);
    }

    public List<Type> getAll() {
        return typeRepository.findAll();
    }

    public Type update(UUID id, CreateType request) {
        Optional<Type> optionalType = typeRepository.findById(id);
        if (optionalType.isPresent()) {
            var existingType = optionalType.get();
            existingType.setName(request.getName());
            return typeRepository.save(existingType);
        }
        return  null;
    }
}
