package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Student;
import org.xiyuan.simply_schedule_backend_monolithic.exception.ResourceNotFoundException;
import org.xiyuan.simply_schedule_backend_monolithic.repository.StudentRepository;
import org.xiyuan.simply_schedule_backend_monolithic.security.ValidateGoogleAuthToken;
import org.xiyuan.simply_schedule_backend_monolithic.service.StudentService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Student", "email", email));
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
    public Student handleGoogleSignIn(final String jwt) {
        Student student = ValidateGoogleAuthToken.verifyGoogleAuthToken(jwt)
                .orElseThrow(() -> new AccessDeniedException("Failed to validate JWT."));
        Optional<Student> isAlreadyStudent = studentRepository.findByEmail(student.getEmail());
        return isAlreadyStudent.orElseGet(() -> studentRepository.saveAndFlush(student));
    }

    @Override
    public Student getUserFromJwt(JwtAuthenticationToken principal) {
        try {
            Student student = ValidateGoogleAuthToken.verifyGoogleAuthToken(principal.getToken().getTokenValue())
                    .orElseThrow(() -> new RuntimeException("Failed to validate JWT."));
            return studentRepository.findByEmail(student.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "Email", student.getEmail()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
