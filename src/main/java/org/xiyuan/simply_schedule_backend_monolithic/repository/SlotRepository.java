package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SlotRepository extends JpaRepository<Slot, UUID> {

    Optional<List<Slot>> findSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId);

    Optional<List<Slot>> findSlotsByCoachId(UUID coachId);

    @Transactional
    void deleteSlotsByStudentIdAndCoachIdAndStatus(UUID studentId, UUID coachId, SlotStatus status);

}
