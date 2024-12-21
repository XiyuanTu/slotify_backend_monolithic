package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import org.xiyuan.simply_schedule_backend_monolithic.entity.Appointment;
import org.xiyuan.simply_schedule_backend_monolithic.exception.ResourceNotFoundException;
import org.xiyuan.simply_schedule_backend_monolithic.repository.AppointmentRepository;
import org.xiyuan.simply_schedule_backend_monolithic.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    @Override
    public List<Appointment> getAppointmentsByCoachId(Long coachId) {
        return appointmentRepository.findAppointmentsByCoachId(coachId).orElseThrow(() -> new ResourceNotFoundException("Appointment", "coachId", String.valueOf(coachId)));
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", String.valueOf(id)));
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public void deleteAppointmentById(Long id) {
        getAppointmentById(id);
        appointmentRepository.deleteById(id);
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        getAppointmentById(appointment.getId());
        appointmentRepository.save(appointment);
    }
}
