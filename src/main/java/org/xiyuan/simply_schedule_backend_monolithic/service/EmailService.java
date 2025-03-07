package org.xiyuan.simply_schedule_backend_monolithic.service;

import jakarta.mail.MessagingException;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;

import java.io.IOException;

public interface EmailService {

    void sendEmail(Slot slot, String baseUrl) throws MessagingException, IOException;


    void sendOpenHourUpdateEmail(Coach coach) throws MessagingException, IOException;

}
