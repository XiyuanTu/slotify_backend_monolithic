package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiyuan.simply_schedule_backend_monolithic.entity.OpenHour;
import org.xiyuan.simply_schedule_backend_monolithic.exception.ResourceNotFoundException;
import org.xiyuan.simply_schedule_backend_monolithic.repository.OpenHourRepository;
import org.xiyuan.simply_schedule_backend_monolithic.service.OpenHourService;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OpenHourServiceImpl implements OpenHourService {
    private final OpenHourRepository openHourRepository;
    @Override
    public List<OpenHour> getOpenHoursByCoachId(UUID coachId) {
        return openHourRepository.findOpenHoursByCoachId(coachId).orElseThrow(() -> new ResourceNotFoundException("OpenHour", "coachId", String.valueOf(coachId)));
    }

    @Override
    public OpenHour getOpenHourById(UUID id) {
        return openHourRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OpenHour", "id", String.valueOf(id)));
    }

    @Override
    public List<OpenHour> createOpenHours(UUID coachId, List<OpenHour> openHours) {
        return openHourRepository.saveAll(openHours);
    }

    @Override
    public void deleteOpenHourById(UUID id) {
        getOpenHourById(id);
        openHourRepository.deleteById(id);
    }

    @Override
    public void deleteOpenHoursByCoachId(UUID coachId) {
        openHourRepository.deleteOpenHoursByCoachId(coachId);
    }
}
