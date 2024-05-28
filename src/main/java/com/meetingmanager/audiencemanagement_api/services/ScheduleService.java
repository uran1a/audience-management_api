package com.meetingmanager.audiencemanagement_api.services;

import com.meetingmanager.audiencemanagement_api.models.freeAudience.FreeAudienceRequest;
import com.meetingmanager.audiencemanagement_api.models.freeAudience.FreeAudienceResponse;
import com.meetingmanager.audiencemanagement_api.models.schedule.Schedule;
import com.meetingmanager.audiencemanagement_api.models.schedule.ScheduleDTO;
import com.meetingmanager.audiencemanagement_api.models.scheduleGaps.Gap;
import com.meetingmanager.audiencemanagement_api.models.scheduleGaps.ScheduleGapsRequest;
import com.meetingmanager.audiencemanagement_api.models.scheduleGaps.ScheduleGapsResponse;
import com.meetingmanager.audiencemanagement_api.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    AudienceService audienceService;

    public Schedule create(Schedule schedule){
        schedule.setId(UUID.randomUUID());
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAll(){
        return scheduleRepository.findAll();
    }
    public boolean delete(UUID id){
        try {
            scheduleRepository.deleteById(id);
        }catch (Exception ex){
            System.out.println("Request error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public Schedule update(UUID id, Schedule updateAudience) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isPresent()) {
            var existingAudience = optionalSchedule.get();

            existingAudience.setAudience_id(updateAudience.getAudience_id());
            existingAudience.setMeeting_id(updateAudience.getMeeting_id());
            existingAudience.setStart_time(updateAudience.getStart_time());
            existingAudience.setEnd_time(updateAudience.getEnd_time());

            return scheduleRepository.save(existingAudience);
        }
        return  null;
    }

    public Schedule getById(UUID id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public List<Schedule> getByAudienceId(UUID id) {
        var schedules = scheduleRepository.findAll();
        return schedules.stream()
                .filter(x -> x.getAudience_id().equals(id))
                .collect(Collectors.toList());
    }

    //!!!
    public ScheduleGapsResponse getScheduleGaps(UUID audienceId, LocalDate requiredDate) {
        List<Schedule> schedules = getScheduleMeetings(audienceId, requiredDate);

        return ScheduleGapsResponse.builder()
                .audience_id(audienceId)
                .gaps(findGapsBetweenMeetings(schedules, requiredDate))
                .build();
    }

    public List<FreeAudienceResponse> getFreeAudiences(FreeAudienceRequest request){
        var scheduleMeetings = getScheduleMeetings(request);

        var audienceByScheduleMeetings = sortScheduleMeetingsByAudiences(scheduleMeetings);

        var freeAudiences = new ArrayList<FreeAudienceResponse>();
        var nearestAvailableAudience = FreeAudienceResponse.builder()
                .start_time(LocalDateTime.of(
                        request.getStart_free_time().getYear(),
                        request.getStart_free_time().getMonth(),
                        request.getStart_free_time().getDayOfMonth(),
                        0, 0 ,0
                )).build();

        for (var audience : audienceByScheduleMeetings.entrySet()){
            var gaps = findGapsBetweenMeetings(audience.getValue(), request.getStart_free_time().toLocalDate());

            var foundGap = findGapBetweenMeetings(gaps, request.getStart_free_time());

            if(foundGap.getKey() != null){
                freeAudiences.add(FreeAudienceResponse.builder()
                        .id(audience.getKey())
                        .start_time(foundGap.getKey().getStart())
                        .end_time(foundGap.getKey().getEnd())
                        .build());
            }
            if(foundGap.getValue() != null){
                if(foundGap.getValue().getStart().isAfter(nearestAvailableAudience.getStart_time())) {
                    nearestAvailableAudience = FreeAudienceResponse.builder()
                            .id(audience.getKey())
                            .start_time(foundGap.getValue().getStart())
                            .end_time(foundGap.getKey().getEnd()).build();
                }
            }
        }

        if(freeAudiences.size() == 0){
            if(nearestAvailableAudience.getId() != null)
                freeAudiences.add(nearestAvailableAudience);
        }

        return freeAudiences;
    }

    private Map<UUID, List<Schedule>> sortScheduleMeetingsByAudiences(List<Schedule> schedules) {
        var scheduleMeetingsByAudiences = new HashMap<UUID, List<Schedule>>();
        for (var schedule : schedules) {
            if(!scheduleMeetingsByAudiences.containsKey(schedule.getAudience_id())){
                scheduleMeetingsByAudiences.put(schedule.getAudience_id(), new ArrayList<>());
                scheduleMeetingsByAudiences.get(schedule.getAudience_id()).add(schedule);
            }
            else{
                var scheduleMeetings = scheduleMeetingsByAudiences.get(schedule.getAudience_id());

                for (int i = 0; i < scheduleMeetings.size(); i++){
                    if(scheduleMeetings.get(i).getEnd_time().isBefore(schedule.getStart_time()))
                        scheduleMeetings.add(i + 1, schedule);
                }
            }
        }

        return scheduleMeetingsByAudiences;
    }

    private List<Schedule> getScheduleMeetings(UUID id, LocalDate date){
        var startDateTime = LocalDateTime.of(
                date.getYear(),
                date.getMonth(),
                date.getDayOfMonth(),
                0, 0, 0);
        return this.getAll().stream()
                .filter(x -> x.getAudience_id().equals(id) && x.getStart_time().isAfter(startDateTime) && x.getEnd_time().isBefore(startDateTime.plusDays(1)))
                .map(x -> Schedule.builder()
                        .id(x.getId())
                        .meeting_id(x.getMeeting_id())
                        .audience_id(x.getAudience_id())
                        .start_time(x.getStart_time())
                        .end_time(x.getEnd_time())
                        .build()
                )
                .collect(Collectors.toList());
    }
    private List<Schedule> getScheduleMeetings(FreeAudienceRequest request){
        var schedules = this.getAll();

        /*
        var now = LocalDateTime.now();
        schedules = schedules.stream()
                .filter(x -> x.getStart_time().isAfter(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0)))
                .toList();
         */

        if(request.getAudience_capacity() > 0){
            schedules = schedules.stream()
                    .filter((x) -> audienceService.getById(x.getAudience_id()).getCapacity() >= request.getAudience_capacity())
                    .toList();
        }

        if(request.getAudience_type_id() != null){
            schedules = schedules.stream()
                    .filter(x -> audienceService.getAudienceById(x.getAudience_id()).getType_id().equals(request.getAudience_type_id()))
                    .toList();
        }

        return schedules;
    }
    private List<Gap> findGapsBetweenMeetings(List<Schedule> meetings, LocalDate date) {
        var startDateTime = LocalDateTime.of(
                date.getYear(),
                date.getMonth(),
                date.getDayOfMonth(),
                8, 0, 0);

        meetings.add(0, Schedule.builder()
                .end_time(startDateTime).build());
        meetings.add(Schedule.builder()
                .start_time(startDateTime.plusHours(14))
                .build());

        var gaps = new ArrayList<Gap>();

        for (int i = 0; i < meetings.size() - 1; i++) {
            LocalDateTime currentEnd = meetings.get(i).getEnd_time();
            LocalDateTime nextStart = meetings.get(i + 1).getStart_time();
            if (!currentEnd.isEqual(nextStart) && currentEnd.isBefore(nextStart)) {
                gaps.add(Gap.builder()
                            .start(currentEnd)
                            .end(nextStart)
                            .build());
            }
        }

        return gaps;
    }

    private Map.Entry<Gap, Gap> findGapBetweenMeetings(List<Gap> gaps, LocalDateTime dateTime) {
        for (int i =0; i < gaps.size(); i++){
            if((gaps.get(i).getStart().isBefore(dateTime) || gaps.get(i).getStart().equals(dateTime))
                    && gaps.get(i).getEnd().isAfter(dateTime)){
                if(i < gaps.size() - 1)
                    return new AbstractMap.SimpleEntry<>(gaps.get(i), gaps.get(i + 1));

                new AbstractMap.SimpleEntry<>(gaps.get(i), null);
            }
        }
        return new AbstractMap.SimpleEntry<>(null, null);
    }
}
