package org.xiyuan.simply_schedule_backend_monolithic.service;

import jakarta.transaction.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.User;

import java.util.List;
import java.util.UUID;

public interface SlotService {
    List<Slot> getSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId, User user);

    List<Slot> getSlotsByCoachId(UUID coachId, User user);

    Slot getSlotById(UUID id);

    List<Slot> createSlots(List<Slot> slots);

    void deleteSlotById(UUID id, User user);

    Slot updateSlotStatus(UUID id, SlotStatus status);

    String updateSlotStatusViaEmail(UUID id, String token, SlotStatus status);

    @Transactional
    void deleteSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId);
}
