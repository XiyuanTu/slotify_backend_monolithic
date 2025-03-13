package org.xiyuan.simply_schedule_backend_monolithic.service;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.springframework.security.oauth2.jwt.Jwt;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.User;
import org.xiyuan.simply_schedule_backend_monolithic.payload.user.StudentDto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    Student getStudentById(UUID studentId);

    Coach getCoachById(UUID coachId);

    @CanIgnoreReturnValue
    Student getStudentByEmail(String email);

    Set<Student> getStudentsByCoachId(UUID coachId);

    Map<Student, Long> getAvailableStudents(UUID coachId);

    Coach getCoachByEmail(String email);

    Student getStudentByEmailOrElseCreateOne(String email, String name, String picture);

    Student createStudent(Student student);

    void deleteStudentByEmail(String email);

    void handleGoogleSignIn(Jwt jwt);

    User getUserFromJwt(Jwt jwt);

    Coach updateCoachById(UUID id, Coach coach);

    void deleteStudents(List<UUID> ids);

    void addCoachToStudent(UUID studentId, String invitationCode);

    Student updateStudent(StudentDto studentDto);

    Coach deleteStudentsFromCoach(UUID coachId, List<UUID> studentIds);
}
