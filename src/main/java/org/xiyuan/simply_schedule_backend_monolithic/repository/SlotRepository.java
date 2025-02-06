package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SlotRepository extends JpaRepository<Slot, UUID> {

    Optional<List<Slot>> findSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId);

    Optional<List<Slot>> findSlotsByCoachId(UUID coachId);

    @Transactional
    void deleteSlotsByStudentIdAndCoachIdAndStatus(UUID studentId, UUID coachId, SlotStatus status);

    @Query("""
             SELECT DISTINCT s.student
              FROM Slot s
              WHERE s.coach.id = :coachId
                AND s.startAt > CURRENT_TIMESTAMP
                AND s.status = "AVAILABLE"
                AND s.student NOT IN (
                    SELECT s2.student
                    FROM Slot s2
                    WHERE s2.coach.id = :coachId
                      AND s2.status IN (
                              org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus.PENDING,
                              org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus.APPOINTMENT
                          )
                )
            """)
    Optional<List<Student>> findAvailableStudents(
            @Param("coachId") UUID coachId
    );
}
