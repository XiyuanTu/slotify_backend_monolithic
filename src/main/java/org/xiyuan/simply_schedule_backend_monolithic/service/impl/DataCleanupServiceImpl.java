package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.repository.OpenHourRepository;
import org.xiyuan.simply_schedule_backend_monolithic.repository.SlotRepository;
import org.xiyuan.simply_schedule_backend_monolithic.service.DataCleanupService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DataCleanupServiceImpl implements DataCleanupService {
    private final SlotRepository slotRepository;
    private final OpenHourRepository openHourRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void deleteOldDataWeekly() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusWeeks(1);
        slotRepository.deleteSlotsByEndAtBefore(cutoffDate);
        openHourRepository.deleteOpenHoursByEndAtBefore(cutoffDate);
    }
}
