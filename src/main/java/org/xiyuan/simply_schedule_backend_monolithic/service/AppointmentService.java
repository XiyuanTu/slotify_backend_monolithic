package org.xiyuan.simply_schedule_backend_monolithic.service;

import org.xiyuan.simply_schedule_backend_monolithic.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAppointmentsByCoachId(Long id);
    Appointment getAppointmentById(Long id);
    Appointment createAppointment(Appointment appointment);

    void deleteAppointmentById(Long id);

    void updateAppointment(Appointment appointment);
}
