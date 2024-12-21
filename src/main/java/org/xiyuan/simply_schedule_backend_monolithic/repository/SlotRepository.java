package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

    Optional<List<Slot>> findSlotsByStudentIdAndCoachId(Long studentId, Long coachId);
    Optional<List<Slot>> findSlotsByCoachId(Long coachId);

    @Transactional
    void deleteSlotsByStudentIdAndCoachIdAndStatus(Long studentId, Long coachId, SlotStatus status);

}
