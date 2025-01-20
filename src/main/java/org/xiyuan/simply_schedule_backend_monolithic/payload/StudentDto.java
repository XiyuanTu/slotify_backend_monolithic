package org.xiyuan.simply_schedule_backend_monolithic.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "Student"
)
public class StudentDto {
    private Long id;

    @Schema(
            description = "Student's name",
            example = "Xiyuan Tu"
    )
    @NotNull()
    private String name;

    @Schema(
            description = "Student's email",
            example = "xiyuan.tyler@gmail.com"
    )
    @NotNull()
    @Email
    private String email;

    @Schema(
            description = "Student's avatar url",
            example = "https://lh3.googleusercontent.com/a/ACg8ocLJ3jmpfPUrzU7DtF_JfOXEaWuCD9PQRdwrMZ54SxUwV9xF3g=s96-c"
    )
    @NotNull()
    private String picture;

}
