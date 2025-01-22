package org.xiyuan.simply_schedule_backend_monolithic.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.xiyuan.simply_schedule_backend_monolithic.constant.FrontendSource;
import org.xiyuan.simply_schedule_backend_monolithic.entity.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
//@Entity
@NoArgsConstructor
@AllArgsConstructor
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public class User extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "picture", nullable = false)
    private String picture;

    @Transient
    private FrontendSource source;
}
