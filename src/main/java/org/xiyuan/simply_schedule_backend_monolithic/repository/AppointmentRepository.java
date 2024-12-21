package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.xiyuan.simply_schedule_backend_monolithic.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<List<Appointment>> findAppointmentsByCoachId(Long coachId);
}
