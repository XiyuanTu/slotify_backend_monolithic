package org.xiyuan.simply_schedule_backend_monolithic.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "Time slot"
)
public class SlotDto {

    private UUID id;
    @Schema(
            description = "Student's id in User table",
            example = "324243252"
    )
    private UUID studentId;

    @Schema(
            description = "Coach's id in User table",
            example = "324243252"
    )
    private UUID coachId;

    @Schema(
            description = "Class' id the slot belongs to",
            example = "324243252"
    )
    private UUID classId;

    @Schema(
            description = "The start of the slot",
            example = "2024-02-01T15:09:00.9920024"
    )
    @NotNull(message = "StartAt can't be empty")
    @FutureOrPresent
    private LocalDateTime startAt;

    @Schema(
            description = "The end of the slot",
            example = "2024-02-01T15:09:00.9920024"
    )
    @NotNull(message = "EndAt can't be empty")
    @FutureOrPresent
    private LocalDateTime endAt;

    private SlotStatus status;

    @Schema(
            description = "If the student has deleted the cancelled slot",
            example = "false"
    )
    @NotNull(message = "StudentDeleted can't be empty")
    private Boolean studentDeleted = false;

    @Schema(
            description = "If the coach has deleted the cancelled slot",
            example = "false"
    )
    @NotNull(message = "CoachDeleted can't be empty")
    private Boolean coachDeleted = false;
}
