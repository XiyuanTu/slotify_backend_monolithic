package org.xiyuan.simply_schedule_backend_monolithic.service;


import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.xiyuan.simply_schedule_backend_monolithic.entity.OpenHour;

import java.util.List;
import java.util.UUID;

public interface OpenHourService {

    List<OpenHour> getOpenHoursByCoachId(UUID coachId);

    @CanIgnoreReturnValue
    OpenHour getOpenHourById(UUID id);

    List<OpenHour> createOpenHours(UUID coachId, List<OpenHour> openHours);

    void deleteOpenHourById(UUID id);

    void deleteOpenHoursByCoachId(UUID coachId);
}
