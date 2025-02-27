package org.xiyuan.simply_schedule_backend_monolithic.service;

import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;

public interface EmailTokenService {
    String generateToken(Slot slot);

    boolean validateToken(String token);

    void deleteToken(String token);

    void deleteTokenBySlot(Slot slot);
}
