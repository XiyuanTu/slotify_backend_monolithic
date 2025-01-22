package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;

import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    Optional<Coach> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);
}
