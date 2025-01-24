package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.constant.FrontendSource;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.User;
import org.xiyuan.simply_schedule_backend_monolithic.exception.ResourceNotFoundException;
import org.xiyuan.simply_schedule_backend_monolithic.repository.CoachRepository;
import org.xiyuan.simply_schedule_backend_monolithic.repository.StudentRepository;
import org.xiyuan.simply_schedule_backend_monolithic.security.GoogleAuthTokenVerifier;
import org.xiyuan.simply_schedule_backend_monolithic.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${coach_email}")
    private String coachEmail;

    private final StudentRepository studentRepository;
    private final CoachRepository coachRepository;
    private final GoogleAuthTokenVerifier googleAuthTokenVerifier;

    @Override
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Student", "email", email));
    }

    @Override
    public Coach getCoachByEmail(String email) {
        return coachRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Coach", "email", email));
    }


    @Override
    public Student getStudentByEmailOrElseCreateOne(String email, String name, String picture) {
        return studentRepository.findByEmail(email).orElseGet(() -> {
            // If student doesn't exist, create a new one
            Student newStudent = new Student();
            newStudent.setEmail(email);
            newStudent.setName(name);
            newStudent.setPicture(picture);
            return studentRepository.save(newStudent);
        });
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudentByEmail(String email) {
        getStudentByEmail(email);
        studentRepository.deleteByEmail(email);
    }

    @Override
    @Transactional
    public void handleGoogleSignIn(final String jwt) {
        User user = googleAuthTokenVerifier.verifyGoogleAuthToken(jwt)
                .orElseThrow(() -> new AccessDeniedException("Failed to validate JWT."));
        String email = user.getEmail();
        if (FrontendSource.CLIENT.equals(user.getSource())) {
            Student student = (Student) user;
            student.setCoach(coachRepository.findByEmail(coachEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Coach", "Email", email)));
            studentRepository.findByEmail(email).orElseGet(() -> studentRepository.saveAndFlush(student));
            return;
        }
        coachRepository.findByEmail(email).orElseGet(() -> coachRepository.saveAndFlush((Coach) user));
    }

    @Override
    public User getUserFromJwt(JwtAuthenticationToken principal) {
        try {
            User user = googleAuthTokenVerifier.verifyGoogleAuthToken(principal.getToken().getTokenValue())
                    .orElseThrow(() -> new RuntimeException("Failed to validate JWT."));
            String email = user.getEmail();
            if (FrontendSource.CLIENT.equals(user.getSource())) {
                Student student = studentRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("Student", "Email", email));
                student.setSource(FrontendSource.CLIENT);
                return student;
            }
            Coach coach = coachRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Coach", "Email", email));
            coach.setSource(FrontendSource.ADMIN);
            return coach;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
