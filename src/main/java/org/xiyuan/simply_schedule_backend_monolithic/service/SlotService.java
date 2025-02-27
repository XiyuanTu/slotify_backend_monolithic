package org.xiyuan.simply_schedule_backend_monolithic.service;

import jakarta.transaction.Transactional;
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;

import java.util.List;
import java.util.UUID;

public interface SlotService {
    List<Slot> getSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId);

    List<Slot> getSlotsByCoachId(UUID coachId);

    Slot getSlotById(UUID id);

    List<Slot> createSlots(List<Slot> slots);

    @Transactional
    void deleteSlotById(UUID id);

    Slot updateSlotStatus(UUID id, SlotStatus status);

    String updateSlotStatusViaEmail(UUID id, String token, SlotStatus status);
    void deleteSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId);
}
