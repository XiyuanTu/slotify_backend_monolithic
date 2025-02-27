package org.xiyuan.simply_schedule_backend_monolithic.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.exception.ResourceNotFoundException;
import org.xiyuan.simply_schedule_backend_monolithic.repository.SlotRepository;
import org.xiyuan.simply_schedule_backend_monolithic.service.EmailService;
import org.xiyuan.simply_schedule_backend_monolithic.service.EmailTokenService;
import org.xiyuan.simply_schedule_backend_monolithic.service.SlotService;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SlotServiceImpl implements SlotService {
    private final EmailService emailService;
    private final EmailTokenService emailTokenService;
    private final SlotRepository slotRepository;
//    private final StudentRepository studentRepository;
//    private final CoachRepository coachRepository;

    @Override
    public List<Slot> getSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId) {
        return slotRepository.findSlotsByStudentIdAndCoachId(studentId, coachId).orElseThrow(() -> new ResourceNotFoundException("Slot", "studentId/coachId", studentId + "/" + coachId));
    }

    @Override
    public Slot getSlotById(UUID id) {
        return slotRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slot", "id", String.valueOf(id)));
    }

    @Override
    public List<Slot> createSlots(List<Slot> slots) {
        // All slots should be in the same status. If not AVAILABLE, then it should already have a class id
        if (!slots.isEmpty() && slots.get(0).getStatus().equals(SlotStatus.AVAILABLE)) {
            UUID classId = UUID.randomUUID();
            slots.forEach(slot -> slot.setClassId(classId));
        }
        List<Slot> newSlots = slotRepository.saveAll(slots);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/api/v1/slot";
        newSlots.forEach(slot -> {
            if (slot.getStatus().equals(SlotStatus.PENDING)) {
                try {
                    emailService.sendEmail(slot, baseUrl);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        return newSlots;
    }

    @Override
    public List<Slot> getSlotsByCoachId(UUID coachId) {
        return slotRepository.findSlotsByCoachId(coachId).orElseThrow(() -> new ResourceNotFoundException("Slot", "coachId", String.valueOf(coachId)));
    }

    @Override
    @Transactional
    public void deleteSlotById(UUID id) {
        Slot slot = getSlotById(id);
        emailTokenService.deleteTokenBySlot(slot);
        slotRepository.deleteById(id);
    }

    @Override
    public Slot updateSlotStatus(UUID id, SlotStatus status) {
        Slot slot = slotRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slot", "id", String.valueOf(id)));
        slot.setStatus(status);
        Slot updatedSlot = slotRepository.save(slot);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/api/v1/slot";
        try {
            emailService.sendEmail(updatedSlot, baseUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return updatedSlot;
    }

    @Override
    public void deleteSlotsByStudentIdAndCoachId(UUID studentId, UUID coachId) {
        slotRepository.deleteSlotsByStudentIdAndCoachIdAndStatus(studentId, coachId, SlotStatus.AVAILABLE);
    }

    @Override
    public String updateSlotStatusViaEmail(UUID id, String token, SlotStatus status) {
        if (!emailTokenService.validateToken(token)) {
            return null;
        }
        emailTokenService.deleteToken(token);
        updateSlotStatus(id, status);
        return switch (status) {
            case APPOINTMENT -> "Appointment Confirmed!";
            case REJECTED -> "Appointment Rejected!";
            case CANCELLED -> "Appointment Cancelled!";
            default -> null;
        };
    }
}
