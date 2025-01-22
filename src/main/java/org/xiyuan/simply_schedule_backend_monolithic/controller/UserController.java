package org.xiyuan.simply_schedule_backend_monolithic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiyuan.simply_schedule_backend_monolithic.constant.FrontendSource;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.User;
import org.xiyuan.simply_schedule_backend_monolithic.payload.ErrorDto;
import org.xiyuan.simply_schedule_backend_monolithic.payload.user.CoachDto;
import org.xiyuan.simply_schedule_backend_monolithic.payload.user.StudentDto;
import org.xiyuan.simply_schedule_backend_monolithic.payload.user.UserDto;
import org.xiyuan.simply_schedule_backend_monolithic.security.CurrentUser;

@RestController
@RequestMapping(path = "/api/v1/user", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD for students"
)
public class UserController {
    private final ModelMapper modelMapper;

    @GetMapping("")
    @Operation(
            summary = "Get student by email"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Student fetched"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    }
    )
    public ResponseEntity<UserDto> getUser(@CurrentUser User user) {
        if (FrontendSource.CLIENT.equals(user.getSource())) {
            StudentDto studentDtoResponse = modelMapper.map(user, StudentDto.class);
            return new ResponseEntity<>(studentDtoResponse, HttpStatus.OK);
        }

        CoachDto coachDtoResponse = modelMapper.map(user, CoachDto.class);
        return new ResponseEntity<>(coachDtoResponse, HttpStatus.OK);
    }
}
