package org.xiyuan.simply_schedule_backend_monolithic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "coach", nullable = false)
    private Coach coach;

    @Column(name = "class_id", nullable = false)
    private UUID classId = UUID.randomUUID();

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    @Column(name = "coach_deleted", nullable = false)
    private Boolean coachDeleted;

    @Column(name = "student_deleted", nullable = false)
    private Boolean studentDeleted;
}
