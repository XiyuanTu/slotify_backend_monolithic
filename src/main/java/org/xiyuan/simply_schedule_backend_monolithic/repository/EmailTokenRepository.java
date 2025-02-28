package org.xiyuan.simply_schedule_backend_monolithic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.xiyuan.simply_schedule_backend_monolithic.entity.EmailToken;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, UUID> {
    Optional<EmailToken> findEmailTokenBySlot(Slot Slot);

    void deleteEmailTokenBySlot(Slot slot);
}
