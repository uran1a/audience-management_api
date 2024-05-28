package com.meetingmanager.audiencemanagement_api.services;

import com.meetingmanager.audiencemanagement_api.models.audience.Audience;
import com.meetingmanager.audiencemanagement_api.models.audience.AudienceResponse;
import com.meetingmanager.audiencemanagement_api.models.audience.CreateAudience;
import com.meetingmanager.audiencemanagement_api.models.audience.UpdateAudience;
import com.meetingmanager.audiencemanagement_api.repositories.AudienceRepository;
import com.meetingmanager.audiencemanagement_api.repositories.ScheduleRepository;
import com.meetingmanager.audiencemanagement_api.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AudienceService {
    @Autowired
    private AudienceRepository audienceRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TypeRepository audienceTypeRepository;

    public Audience create(CreateAudience createAudience){
        var type = audienceTypeRepository.findById(createAudience.getType_id());
        if(type.isPresent()){
            var audience = Audience.builder()
                    .id(UUID.randomUUID())
                    .building(createAudience.getBuilding())
                    .floor(createAudience.getFloor())
                    .room(createAudience.getRoom())
                    .capacity(createAudience.getCapacity())
                    .type_id(type.get().getId())
                    .build();

            return audienceRepository.save(audience);
        }
        return null;
    }

    public Audience update(UUID id, UpdateAudience updatedAudience){
        Optional<Audience> optionalAudience = audienceRepository.findById(id);
        if (optionalAudience.isPresent()) {
            Audience existingAudience = optionalAudience.get();
            existingAudience.setBuilding(updatedAudience.getBuilding());
            existingAudience.setFloor(updatedAudience.getFloor());
            existingAudience.setRoom(updatedAudience.getRoom());
            existingAudience.setCapacity(updatedAudience.getCapacity());
            existingAudience.setType_id(updatedAudience.getType_id());

            return audienceRepository.save(existingAudience);
        }
        return  null;
    }

    public List<AudienceResponse> getAll(){
        var types = audienceTypeRepository.findAll();
        var audiences = audienceRepository.findAll();

        var response = new ArrayList<AudienceResponse>();
        for (var audience : audiences){

            var type = types.stream()
                    .filter(p -> p.getId().equals(audience.getType_id()))
                    .findFirst()
                    .orElse(null);
            if(type == null)
                return null;

            response.add(AudienceResponse.builder()
                    .id(audience.getId())
                    .building(audience.getBuilding())
                    .floor(audience.getFloor())
                    .room(audience.getRoom())
                    .capacity(audience.getCapacity())
                    .typeName(type.getName())
                    .build()
            );
        }
        return response;
    }

    public boolean delete(UUID id){
        try {
            var schedules = scheduleRepository.findAll().stream()
                    .filter(x -> x.getAudience_id().equals(id))
                    .toList();

            if(schedules.size() > 0){
                scheduleRepository.deleteAll(schedules);
            }

            audienceRepository.deleteById(id);
        }catch (Exception ex){
            System.out.println("Request error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public AudienceResponse getById(UUID id) {
        var types = audienceTypeRepository.findAll();
        Optional<Audience> optionalAudience = audienceRepository.findById(id);
        if(optionalAudience.isPresent()) {
            var audience = optionalAudience.get();
            var type = types.stream()
                    .filter(p -> p.getId().equals(audience.getType_id()))
                    .findFirst()
                    .orElse(null);
            if (type == null)
                return null;

            return AudienceResponse.builder()
                    .id(audience.getId())
                    .building(audience.getBuilding())
                    .floor(audience.getFloor())
                    .room(audience.getRoom())
                    .capacity(audience.getCapacity())
                    .typeName(type.getName())
                    .build();
        }
        return null;
    }

    public Audience getAudienceById(UUID id) {
        var types = audienceTypeRepository.findAll();
        Optional<Audience> optionalAudience = audienceRepository.findById(id);
        if(optionalAudience.isPresent()) {
            var audience = optionalAudience.get();
            var type = types.stream()
                    .filter(p -> p.getId().equals(audience.getType_id()))
                    .findFirst()
                    .orElse(null);
            if (type == null)
                return null;

            return audience;
        }
        return null;
    }
}
