package org.xiyuan.simply_schedule_backend_monolithic.service;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {
    Student getStudentById(UUID studentId);

    Coach getCoachById(UUID coachId);
    Student getStudentByEmail(String email);

    List<Student> getStudentsByCoachId(UUID coachId);

    Map<Student, Long> getAvailableStudents(UUID coachId);

    Coach getCoachByEmail(String email);

    Student getStudentByEmailOrElseCreateOne(String email, String name, String picture);

    Student createStudent(Student student);

    void deleteStudentByEmail(String email);

    void handleGoogleSignIn(final String jwt);

    User getUserFromJwt(JwtAuthenticationToken principal);
}
