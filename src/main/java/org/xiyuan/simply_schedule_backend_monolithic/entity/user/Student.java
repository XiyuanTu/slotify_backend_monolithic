package org.xiyuan.simply_schedule_backend_monolithic.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.xiyuan.simply_schedule_backend_monolithic.constant.Location;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    @ManyToOne
    @JoinColumn(name = "coach", nullable = false)
    // for now, each student has only one coach
    private Coach coach;

    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    private Location location;
}
