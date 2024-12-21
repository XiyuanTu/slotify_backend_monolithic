package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.xiyuan.simply_schedule_backend_monolithic.entity.OpenHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpenHourRepository  extends JpaRepository<OpenHour, Long> {
    Optional<List<OpenHour>> findOpenHoursByCoachId(Long coachId);

    @Transactional
    void deleteOpenHoursByCoachId(Long coachId);
}
