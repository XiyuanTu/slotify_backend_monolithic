package org.xiyuan.simply_schedule_backend_monolithic.service;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Student;

public interface StudentService {
    Student getStudentByEmail(String email);

    Student getStudentByEmailOrElseCreateOne(String email, String name, String picture);

    Student createStudent(Student student);

    void deleteStudentByEmail(String email);

    Student handleGoogleSignIn(final String jwt);
    Student getUserFromJwt(JwtAuthenticationToken principal);
}
