package org.xiyuan.simply_schedule_backend_monolithic.service;

import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;

import java.util.List;
import java.util.UUID;

public interface SlotService {
    List<Slot> getSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId);

    List<Slot> getSlotsByCoachId(UUID coachId);

    Slot getSlotById(UUID id);

    List<Slot> createSlots(UUID studentId, UUID coachId, List<Slot> slots);

    void deleteSlotById(UUID id);

    void updateSlot(Slot slot);

    void deleteSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId);
}
