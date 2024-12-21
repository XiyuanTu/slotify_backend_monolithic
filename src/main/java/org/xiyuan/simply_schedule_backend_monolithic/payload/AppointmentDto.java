package org.xiyuan.simply_schedule_backend_monolithic.payload;

import org.xiyuan.simply_schedule_backend_monolithic.constant.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "Appointment"
)
public class AppointmentDto {
    private Long id;
    @Schema(
            description = "Student's id in User table",
            example = "324243252"
    )
    @NotNull()
    private Long studentId;

    @Schema(
            description = "Coach's id in User table",
            example = "324243252"
    )
    @NotNull()
    private Long coachId;

    @Schema(
            description = "The start of the appointment",
            example = "2024-02-01T15:09:00.9920024"
    )
    @NotNull(message = "StartAt can't be empty")
    @FutureOrPresent
    private LocalDateTime startAt;

    @Schema(
            description = "The end of the appointment",
            example = "2024-02-01T15:09:00.9920024"
    )
    @NotNull()
    @FutureOrPresent
    private LocalDateTime endAt;

    private AppointmentStatus status;
}
