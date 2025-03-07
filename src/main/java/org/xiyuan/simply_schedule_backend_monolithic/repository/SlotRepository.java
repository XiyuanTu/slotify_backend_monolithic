package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SlotRepository extends JpaRepository<Slot, UUID> {

    Optional<List<Slot>> findSlotsByStudent_IdAndCoach_Id(UUID studentId, UUID coachId);

    Optional<List<Slot>> findSlotsByCoach_Id(UUID coachId);

    void deleteSlotsByStudent_IdAndCoach_IdAndStatus(UUID studentId, UUID coachId, SlotStatus status);

    @Query("""
                SELECT s.student, COUNT(DISTINCT s.classId)
                FROM Slot s
                WHERE s.coach.id = :coachId
                AND NOT EXISTS (
                  SELECT 1
                  FROM Slot s2
                  WHERE s2.classId = s.classId
                    AND (s2.status = 'PENDING' or s2.status = 'APPOINTMENT')
                )
                GROUP BY s.student
            """)
    Optional<List<Object[]>> findAvailableStudents(
            @Param("coachId") UUID coachId
    );

    void deleteSlotsByEndAtBefore(LocalDateTime cutoffDate);

}
