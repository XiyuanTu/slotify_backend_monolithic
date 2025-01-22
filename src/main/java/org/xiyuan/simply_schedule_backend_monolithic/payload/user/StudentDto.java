package org.xiyuan.simply_schedule_backend_monolithic.payload.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Student"
)
public class StudentDto extends UserDto {
    private Coach coach;
}
