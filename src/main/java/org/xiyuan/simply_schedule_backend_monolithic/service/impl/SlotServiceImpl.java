package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.exception.ResourceNotFoundException;
import org.xiyuan.simply_schedule_backend_monolithic.repository.SlotRepository;
import org.xiyuan.simply_schedule_backend_monolithic.service.SlotService;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SlotServiceImpl implements SlotService {
    private final SlotRepository slotRepository;

    @Override
    public List<Slot> getSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId) {
        return slotRepository.findSlotsByStudentIdAndCoachId(studentId, coachId).orElseThrow(() -> new ResourceNotFoundException("Slot", "studentId/coachId", studentId + "/" + coachId));
    }

    @Override
    public Slot getSlotById(UUID id) {
        return slotRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slot", "id", String.valueOf(id)));
    }

    @Override
    public List<Slot> createSlots(UUID studentId, UUID coachId, List<Slot> slots) {
        return slotRepository.saveAll(slots);
    }

    @Override
    public List<Slot> getSlotsByCoachId(UUID coachId) {
        return slotRepository.findSlotsByCoachId(coachId).orElseThrow(() -> new ResourceNotFoundException("Slot", "coachId", String.valueOf(coachId)));
    }

    @Override
    public void deleteSlotById(UUID id) {
        getSlotById(id);
        slotRepository.deleteById(id);
    }

    @Override
    public void updateSlot(Slot slot) {
        getSlotById(slot.getId());
        slotRepository.save(slot);
    }

    @Override
    public void deleteSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId) {
        slotRepository.deleteSlotsByStudentIdAndCoachIdAndStatus(studentId, coachId, SlotStatus.SCHEDULING);
    }
}
