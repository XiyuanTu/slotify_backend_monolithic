package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.xiyuan.simply_schedule_backend_monolithic.constant.FrontendSource;
import org.xiyuan.simply_schedule_backend_monolithic.constant.Location;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.User;
import org.xiyuan.simply_schedule_backend_monolithic.exception.CoachAlreadyAddedException;
import org.xiyuan.simply_schedule_backend_monolithic.exception.ResourceNotFoundException;
import org.xiyuan.simply_schedule_backend_monolithic.payload.user.StudentDto;
import org.xiyuan.simply_schedule_backend_monolithic.repository.CoachRepository;
import org.xiyuan.simply_schedule_backend_monolithic.repository.SlotRepository;
import org.xiyuan.simply_schedule_backend_monolithic.repository.StudentRepository;
import org.xiyuan.simply_schedule_backend_monolithic.security.GoogleAuthTokenVerifier;
import org.xiyuan.simply_schedule_backend_monolithic.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final StudentRepository studentRepository;
    private final CoachRepository coachRepository;
    private final SlotRepository slotRepository;
    private final GoogleAuthTokenVerifier googleAuthTokenVerifier;

    @Override
    public Student getStudentById(UUID studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId.toString()));
    }

    @Override
    public Coach getCoachById(UUID coachId) {
        return coachRepository.findById(coachId).orElseThrow(() -> new ResourceNotFoundException("Student", "id", coachId.toString()));
    }

    @Override
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Student", "email", email));
    }

    @Override
    public Set<Student> getStudentsByCoachId(UUID coachId) {
        return studentRepository.findByDefaultCoach_Id(coachId).orElseThrow(() -> new ResourceNotFoundException("Student", "coachId", coachId.toString()));
    }

    @Override
    public Map<Student, Long> getAvailableStudents(UUID coachId) {
        return slotRepository.findAvailableStudents(coachId).orElseThrow(() -> new ResourceNotFoundException("Student", "coachId", coachId.toString()))
                .stream()
                .collect(Collectors.toMap(
                        row -> (Student) row[0],
                        row -> (Long) row[1]
                ));
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
            student.setLocation(Location.MILPITAS);
            studentRepository.findByEmail(email).orElseGet(() -> studentRepository.saveAndFlush(student));
            return;
        }
        Coach coach = (Coach) user;
        Random rand = new Random();
        coach.setInvitationCode(Integer.toString(100000 + rand.nextInt(900000)));
        coachRepository.findByEmail(email).orElseGet(() -> coachRepository.saveAndFlush(coach));
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

    @Override
    public Coach updateCoachById(UUID id, Coach coach) {
        Coach existingCoach = coachRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student", "id", id.toString()));
        existingCoach.setInvitationCode(coach.getInvitationCode());
        return coachRepository.save(existingCoach);
    }

    @Override
    public void deleteStudents(List<UUID> ids) {
        ids.forEach(studentRepository::deleteById);
    }

    @Override
    public void addCoachToStudent(UUID studentId, String invitationCode) {
        Coach coach = coachRepository.findByInvitationCode(invitationCode).orElseThrow(() -> new ResourceNotFoundException("Coach", "invitation code", invitationCode));
        Student student = getStudentById(studentId);
        if (student.getCoaches().contains(coach)) {
            throw new CoachAlreadyAddedException(student.getName(), coach.getName());
        }
        coach.getStudents().add(student);
        coachRepository.save(coach);
        student.getCoaches().add(coach);
        if (student.getDefaultCoach() == null) {
            student.setDefaultCoach(coach);
        }
        studentRepository.save(student);
    }

    @Override
    public Student updateStudent(StudentDto studentDto) {
        Student student = getStudentById(studentDto.getId());
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setPicture(studentDto.getPicture());
        student.setDefaultCoach(getCoachById(studentDto.getDefaultCoachId()));
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Coach deleteStudentsFromCoach(UUID coachId, List<UUID> studentIds) {
        Coach coach = coachRepository.findById(coachId).orElseThrow(() -> new ResourceNotFoundException("Coach", "id", coachId.toString()));
        coach.getStudents().removeIf(student -> studentIds.contains(student.getId()));
        List<Student> students = studentIds.stream().map(studentId -> {
            Student student = getStudentById(studentId);
            student.getCoaches().remove(coach);
            if (student.getDefaultCoach().equals(coach)) {
                student.setDefaultCoach(null);
            }
            return student;
        }).toList();

        studentRepository.saveAll(students);
        return coachRepository.save(coach);
    }
}
