package org.xiyuan.simply_schedule_backend_monolithic.service;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Appointment;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    List<Appointment> getAppointmentsByCoachId(UUID id);

    @CanIgnoreReturnValue
    Appointment getAppointmentById(UUID id);
    Appointment createAppointment(Appointment appointment);

    void deleteAppointmentById(UUID id);

    void updateAppointment(Appointment appointment);
}
