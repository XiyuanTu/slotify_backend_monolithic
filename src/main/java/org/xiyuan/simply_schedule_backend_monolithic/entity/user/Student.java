package org.xiyuan.simply_schedule_backend_monolithic.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.xiyuan.simply_schedule_backend_monolithic.constant.Location;

import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = "coaches")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {

    @ManyToOne
    @JoinColumn(name = "default_coach")
    private Coach defaultCoach;

    @ManyToMany(mappedBy = "students")
    private Set<Coach> coaches;

    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    private Location location;
}
