package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.entity.OpenHour;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OpenHourRepository extends JpaRepository<OpenHour, UUID> {
    Optional<List<OpenHour>> findOpenHoursByCoach_Id(UUID coachId);

    @Transactional
    void deleteOpenHoursByCoach_Id(UUID coachId);

    void deleteOpenHoursByEndAtBefore(LocalDateTime cutoffDate);
}
