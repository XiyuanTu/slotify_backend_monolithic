package org.xiyuan.simply_schedule_backend_monolithic.constant;

public enum SlotStatus {
    AVAILABLE, PENDING, APPOINTMENT, REJECTED, CANCELLED;

    public boolean isAfter(SlotStatus status) {
        return switch (this) {
            case AVAILABLE, PENDING -> false;
            case APPOINTMENT, REJECTED -> status == SlotStatus.PENDING;
            case CANCELLED -> status == SlotStatus.APPOINTMENT;
        };
    }
}
