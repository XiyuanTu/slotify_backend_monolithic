package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiyuan.simply_schedule_backend_monolithic.entity.OpenHour;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;
import org.xiyuan.simply_schedule_backend_monolithic.exception.ResourceNotFoundException;
import org.xiyuan.simply_schedule_backend_monolithic.repository.CoachRepository;
import org.xiyuan.simply_schedule_backend_monolithic.repository.OpenHourRepository;
import org.xiyuan.simply_schedule_backend_monolithic.service.EmailService;
import org.xiyuan.simply_schedule_backend_monolithic.service.OpenHourService;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OpenHourServiceImpl implements OpenHourService {

    private final OpenHourRepository openHourRepository;
    private final CoachRepository coachRepository;
    private final EmailService emailService;

    @Override
    public List<OpenHour> getOpenHoursByCoachId(UUID coachId) {
        return openHourRepository.findOpenHoursByCoach_Id(coachId).orElseThrow(() -> new ResourceNotFoundException("OpenHour", "coachId", String.valueOf(coachId)));
    }

    @Override
    public OpenHour getOpenHourById(UUID id) {
        return openHourRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OpenHour", "id", String.valueOf(id)));
    }

    @Override
    public List<OpenHour> createOpenHours(UUID coachId, List<OpenHour> openHours) {
        Coach coach = coachRepository.findById(coachId).orElseThrow(() -> new ResourceNotFoundException("Coach", "coachId", String.valueOf(coachId)));
        List<OpenHour> savedOpenHours = openHourRepository.saveAll(openHours);
        try {
            emailService.sendOpenHourUpdateEmail(coach);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return savedOpenHours;
    }

    @Override
    public void deleteOpenHourById(UUID id) {
        getOpenHourById(id);
        openHourRepository.deleteById(id);
    }

    @Override
    public void deleteOpenHoursByCoachId(UUID coachId) {
        openHourRepository.deleteOpenHoursByCoach_Id(coachId);
    }
}
