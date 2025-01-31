package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByEmail(String email);

    Optional<List<Student>> findByCoachId(UUID coachId);
    @Transactional
    void deleteByEmail(String email);
}
