package org.xiyuan.simply_schedule_backend_monolithic.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    @ManyToOne
    @JoinColumn(name = "coach")
    // for now, each student has only one coach
    private Coach coach;
}
