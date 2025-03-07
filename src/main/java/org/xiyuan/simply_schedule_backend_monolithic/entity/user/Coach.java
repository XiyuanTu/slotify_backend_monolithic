package org.xiyuan.simply_schedule_backend_monolithic.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = "students")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Coach extends User {

    @Column(name = "invitation_code", nullable = false)
    private String invitationCode;

    @ManyToMany
    @JoinTable(
            name = "coach_student",
            joinColumns = @JoinColumn(name = "coach"),
            inverseJoinColumns = @JoinColumn(name = "student")
    )
    private Set<Student> students;


    public Set<Student> getStudents() {
        return this.students == null ? new HashSet<>() : students;
    }
}
