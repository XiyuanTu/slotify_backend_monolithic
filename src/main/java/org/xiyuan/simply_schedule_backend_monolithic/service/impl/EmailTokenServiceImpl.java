package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiyuan.simply_schedule_backend_monolithic.entity.EmailToken;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.repository.EmailTokenRepository;
import org.xiyuan.simply_schedule_backend_monolithic.service.EmailTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailTokenServiceImpl implements EmailTokenService {
    private final EmailTokenRepository emailTokenRepository;

    public String generateToken(Slot slot) {
        EmailToken emailToken = emailTokenRepository.findEmailTokenBySlot(slot).orElse(null);
        if (emailToken != null) {
            return emailToken.getId().toString();
        }
        UUID token = UUID.randomUUID();
        LocalDateTime expirationTime = slot.getStartAt(); // subject to change later
        emailToken = new EmailToken(token, slot, expirationTime);

        emailTokenRepository.save(emailToken);

        return token.toString();
    }

    public boolean validateToken(String token) {
        EmailToken emailToken = emailTokenRepository.findById(UUID.fromString(token)).orElse(null);
        return emailToken != null && !emailToken.isExpired();
    }

    public void deleteToken(String token) {
        emailTokenRepository.deleteById(UUID.fromString(token));
    }

    @Override
    public void deleteTokenBySlot(Slot slot) {
        emailTokenRepository.deleteEmailTokenBySlot(slot);
    }
}
