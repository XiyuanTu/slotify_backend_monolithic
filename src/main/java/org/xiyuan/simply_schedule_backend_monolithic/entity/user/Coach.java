package org.xiyuan.simply_schedule_backend_monolithic.entity.user;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Coach extends User {
    @Column(nullable = false)
    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private List<Student> students;

    public List<Student> getStudents() {
        return this.students == null ? new ArrayList<>() : students;
    }
}
