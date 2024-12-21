package org.xiyuan.simply_schedule_backend_monolithic.service;


import org.xiyuan.simply_schedule_backend_monolithic.entity.OpenHour;

import java.util.List;

public interface OpenHourService {

    List<OpenHour> getOpenHoursByCoachId(Long coachId);
    OpenHour getOpenHourById(Long id);
    List<OpenHour> createOpenHours(Long coachId, List<OpenHour> openHours);

    void deleteOpenHourById(Long id);

    void deleteOpenHoursByCoachId(Long coachId);
}
