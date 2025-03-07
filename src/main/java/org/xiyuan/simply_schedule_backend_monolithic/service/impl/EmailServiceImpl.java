package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.User;
import org.xiyuan.simply_schedule_backend_monolithic.service.EmailService;
import org.xiyuan.simply_schedule_backend_monolithic.service.EmailTokenService;
import org.xiyuan.simply_schedule_backend_monolithic.service.UserService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailTokenService emailTokenService;
    private final UserService userService;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final DateTimeFormatter startFormatter = DateTimeFormatter.ofPattern("E MMM d, yyyy h a", Locale.ENGLISH);
    private final DateTimeFormatter endFormatter = DateTimeFormatter.ofPattern("h a", Locale.ENGLISH); // start time and end time must be in the same day
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Value("${frontend_url.student}")
    private String studentFrontendUrl;
    @Value("${frontend_url.coach}")
    private String coachFrontendUrl;

    @Override
    @Async
    public void sendEmail(Slot slot, String baseUrl) throws MessagingException, IOException {
        switch (slot.getStatus()) {
            case PENDING -> sendConfirmationEmail(slot, baseUrl);
            case APPOINTMENT -> sendAppointmentEmail(slot, baseUrl);
            case REJECTED -> sendRejectedEmail(slot);
            case CANCELLED -> sendCancelledEmail(slot);
            default -> System.out.println("unexpected status");
        }
    }

    @Override
    @Async
    public void sendOpenHourUpdateEmail(Coach coach) throws MessagingException, IOException {
        Set<Student> students = coach.getStudents();
        for (Student student : students) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String studentName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());
            String coachName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());
            helper.setFrom(new InternetAddress(senderEmail, "Slotify"));
            helper.setTo(student.getEmail());
            helper.setSubject(coachName + " has updated their open hours!");

            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("studentName", studentName);
            emailVariables.put("coachName", coachName);
            emailVariables.put("websiteUrl", studentFrontendUrl);

            Context context = new Context();
            context.setVariables(emailVariables);
            String emailContent = templateEngine.process("open-hour-update", context);

            helper.setText(emailContent, true);

            mailSender.send(message);
        }
    }

    private void sendConfirmationEmail(Slot slot, String baseUrl) throws MessagingException, UnsupportedEncodingException {
        Student student = slot.getStudent();
        student = userService.getStudentById(student.getId());
        Coach coach = slot.getCoach();
        coach = userService.getCoachById(coach.getId());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String token = emailTokenService.generateToken(slot);
        String studentName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());
        String coachName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());
        helper.setFrom(new InternetAddress(senderEmail, "Slotify"));
        helper.setTo(student.getEmail());
        String time = slot.getStartAt().format(startFormatter) + " - " + slot.getEndAt().format(endFormatter);
        helper.setSubject("Invitation from " + coachName + " @ " + time);

        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("studentName", studentName);
        emailVariables.put("coachName", coachName);
        emailVariables.put("time", time);
        emailVariables.put("websiteUrl", studentFrontendUrl);
        emailVariables.put("acceptUrl", baseUrl + "/" + slot.getId() + "/token/" + token + "?status=APPOINTMENT");
        emailVariables.put("rejectUrl", baseUrl + "/" + slot.getId() + "/token/" + token + "?status=REJECTED");

        Context context = new Context();
        context.setVariables(emailVariables);
        String emailContent = templateEngine.process("confirmation", context);

        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    private void sendAppointmentEmail(Slot slot, String baseUrl) throws MessagingException, IOException {
        String token = emailTokenService.generateToken(slot);
        Student student = slot.getStudent();
        Coach coach = slot.getCoach();
        String studentName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());
        String coachName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());

        // email to student
        MimeMessage studentMessage = mailSender.createMimeMessage();
        MimeMessageHelper studentMessageHelper = new MimeMessageHelper(studentMessage, true);

        studentMessageHelper.setFrom(new InternetAddress(senderEmail, "Slotify"));
        studentMessageHelper.setTo(student.getEmail());
        String time = slot.getStartAt().format(startFormatter) + " - " + slot.getEndAt().format(endFormatter);

        studentMessageHelper.setSubject("Your class with " + coachName + " @ " + time + " confirmed!");

        Map<String, Object> studentEmailVariables = new HashMap<>();
        studentEmailVariables.put("studentName", studentName);
        studentEmailVariables.put("coachName", coachName);
        studentEmailVariables.put("time", time);
        studentEmailVariables.put("websiteUrl", studentFrontendUrl);
        studentEmailVariables.put("cancelUrl", baseUrl + "/" + slot.getId() + "/token/" + token + "?status=CANCELLED");

        Context studentEmailContext = new Context();
        studentEmailContext.setVariables(studentEmailVariables);
        String studentEmailContent = templateEngine.process("appointment-student", studentEmailContext);

        studentMessageHelper.setText(studentEmailContent, true);

        byte[] icsBytesForStudent = generateCalendarInvite(slot.getStartAt(), slot.getEndAt(), "Class with " + coachName, coach);
        ByteArrayResource resourceForStudent = new ByteArrayResource(icsBytesForStudent);
        studentMessageHelper.addAttachment("invite.ics", resourceForStudent);

        mailSender.send(studentMessage);

        // email to coach
        MimeMessage coachMessage = mailSender.createMimeMessage();
        MimeMessageHelper coachMessageHelper = new MimeMessageHelper(coachMessage, true);

        coachMessageHelper.setFrom(new InternetAddress(senderEmail, "Slotify"));
        coachMessageHelper.setTo(coach.getEmail());
        coachMessageHelper.setSubject("Class Confirmed: " + studentName + " has confirmed your class @ " + time);

        Map<String, Object> coachEmailVariables = new HashMap<>();
        coachEmailVariables.put("studentName", studentName);
        coachEmailVariables.put("coachName", coachName);
        coachEmailVariables.put("time", time);
        coachEmailVariables.put("websiteUrl", coachFrontendUrl);
        coachEmailVariables.put("cancelUrl", baseUrl + "/" + slot.getId() + "/token/" + token + "?status=CANCELLED");

        Context coachEmailContext = new Context();
        coachEmailContext.setVariables(coachEmailVariables);
        String coachEmailContent = templateEngine.process("appointment-coach", coachEmailContext);

        coachMessageHelper.setText(coachEmailContent, true);

        byte[] icsBytesForCoach = generateCalendarInvite(slot.getStartAt(), slot.getEndAt(), "Class with " + studentName, student);
        ByteArrayResource resourceForCoach = new ByteArrayResource(icsBytesForCoach);
        coachMessageHelper.addAttachment("invite.ics", resourceForCoach);

        mailSender.send(coachMessage);
    }

    private void sendRejectedEmail(Slot slot) throws MessagingException, UnsupportedEncodingException {
        Student student = slot.getStudent();
        Coach coach = slot.getCoach();
        String studentName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());
        String coachName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());

        // email to student
        MimeMessage studentMessage = mailSender.createMimeMessage();
        MimeMessageHelper studentMessageHelper = new MimeMessageHelper(studentMessage, true);

        studentMessageHelper.setFrom(new InternetAddress(senderEmail, "Slotify"));
        studentMessageHelper.setTo(student.getEmail());
        String time = slot.getStartAt().format(startFormatter) + " - " + slot.getEndAt().format(endFormatter);

        studentMessageHelper.setSubject("Your class with " + coachName + " @ " + time + " rejected!");

        Map<String, Object> studentEmailVariables = new HashMap<>();
        studentEmailVariables.put("studentName", studentName);
        studentEmailVariables.put("coachName", coachName);
        studentEmailVariables.put("time", time);
        studentEmailVariables.put("websiteUrl", studentFrontendUrl);

        Context studentEmailContext = new Context();
        studentEmailContext.setVariables(studentEmailVariables);
        String studentEmailContent = templateEngine.process("rejected-student", studentEmailContext);

        studentMessageHelper.setText(studentEmailContent, true);

        mailSender.send(studentMessage);

        // email to coach
        MimeMessage coachMessage = mailSender.createMimeMessage();
        MimeMessageHelper coachMessageHelper = new MimeMessageHelper(coachMessage, true);

        coachMessageHelper.setFrom(new InternetAddress(senderEmail, "Slotify"));
        coachMessageHelper.setTo(coach.getEmail());
        coachMessageHelper.setSubject("Class Confirmed: " + studentName + " has rejected your class  @ " + time);

        Map<String, Object> coachEmailVariables = new HashMap<>();
        coachEmailVariables.put("studentName", studentName);
        coachEmailVariables.put("coachName", coachName);
        coachEmailVariables.put("time", time);

        Context coachEmailContext = new Context();
        coachEmailContext.setVariables(coachEmailVariables);
        String coachEmailContent = templateEngine.process("rejected-coach", coachEmailContext);

        coachMessageHelper.setText(coachEmailContent, true);

        mailSender.send(coachMessage);
    }

    private void sendCancelledEmail(Slot slot) throws MessagingException, UnsupportedEncodingException {
        Student student = slot.getStudent();
        Coach coach = slot.getCoach();
        String studentName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());
        String coachName = WordUtils.capitalizeFully(coach.getName().split(" ")[0].toLowerCase());

        // email to student
        MimeMessage studentMessage = mailSender.createMimeMessage();
        MimeMessageHelper studentMessageHelper = new MimeMessageHelper(studentMessage, true);

        studentMessageHelper.setFrom(new InternetAddress(senderEmail, "Slotify"));
        studentMessageHelper.setTo(student.getEmail());
        String time = slot.getStartAt().format(startFormatter) + " - " + slot.getEndAt().format(endFormatter);

        studentMessageHelper.setSubject("Your class with " + coachName + " @ " + time + " cancelled!");

        Map<String, Object> studentEmailVariables = new HashMap<>();
        studentEmailVariables.put("studentName", studentName);
        studentEmailVariables.put("coachName", coachName);
        studentEmailVariables.put("time", time);

        Context studentEmailContext = new Context();
        studentEmailContext.setVariables(studentEmailVariables);
        String studentEmailContent = templateEngine.process("cancelled-student", studentEmailContext);

        studentMessageHelper.setText(studentEmailContent, true);

        mailSender.send(studentMessage);

        // email to coach
        MimeMessage coachMessage = mailSender.createMimeMessage();
        MimeMessageHelper coachMessageHelper = new MimeMessageHelper(coachMessage, true);

        coachMessageHelper.setFrom(new InternetAddress(senderEmail, "Slotify"));
        coachMessageHelper.setTo(coach.getEmail());
        coachMessageHelper.setSubject("Class Cancelled: " + studentName + " has cancelled your class  @ " + time);

        Map<String, Object> coachEmailVariables = new HashMap<>();
        coachEmailVariables.put("studentName", studentName);
        coachEmailVariables.put("coachName", coachName);
        coachEmailVariables.put("time", time);

        Context coachEmailContext = new Context();
        coachEmailContext.setVariables(coachEmailVariables);
        String coachEmailContent = templateEngine.process("cancelled-coach", coachEmailContext);

        coachMessageHelper.setText(coachEmailContent, true);

        mailSender.send(coachMessage);
    }

    private byte[] generateCalendarInvite(LocalDateTime start, LocalDateTime end, String eventName, User user) throws IOException {
        // Create a TimeZone
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone("America/Los_Angeles");
        VTimeZone tz = timezone.getVTimeZone();

        VEvent meeting = new VEvent(start, end, eventName);
        // add timezone info..
        meeting.add(tz.getTimeZoneId());

        // generate unique identifier..
        UidGenerator ug = new RandomUidGenerator();
        meeting.add(ug.generateUid());

        // add attendees
        Attendee attendee = new Attendee(URI.create("mailto:" + user.getEmail()));
        attendee.add(Role.REQ_PARTICIPANT);
        attendee.add(new Cn(user.getName()));
        meeting.add(attendee);

        // add organizer
        Organizer organizer = new Organizer(URI.create("mailto:slotify.txy@gmail.com"));
        organizer.add(new Cn("Slotify"));
        meeting.add(organizer);

        Calendar calendar = new Calendar()
                .withProdId("-//Slotify//Slotify 1.0//EN")
                .withDefaults()
                .withComponent(meeting)
                .getFluentTarget();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, bout);
        return bout.toByteArray();
    }
}
