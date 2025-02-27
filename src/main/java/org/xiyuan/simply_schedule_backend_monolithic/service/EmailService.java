package org.xiyuan.simply_schedule_backend_monolithic.service;

import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;

import java.io.IOException;

public interface EmailService {
    @Async
    void sendEmail(Slot slot, String baseUrl) throws MessagingException, IOException;

    void sendOpenHourUpdateEmail(Coach coach) throws MessagingException, IOException;

//    void sendAppointmentConfirmedEmail2Coach(String to, String subject, String body);
//    void sendAppointmentConfirmedEmail2Student(String to, String subject, String body);
//    void sendRejectionEmail2Coach(String to, String subject, String body);
//    void sendRejectionEmail2Student(String to, String subject, String body);
}
