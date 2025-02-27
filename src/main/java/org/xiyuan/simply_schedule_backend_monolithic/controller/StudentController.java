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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;
import org.xiyuan.simply_schedule_backend_monolithic.payload.ErrorDto;
import org.xiyuan.simply_schedule_backend_monolithic.payload.user.StudentDto;
import org.xiyuan.simply_schedule_backend_monolithic.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/student", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD for students"
)
public class StudentController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("/coach/{coachId}")
    @Operation(
            summary = "Get students by coachId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Students fetched"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Students not found"
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
    public ResponseEntity<List<StudentDto>> getStudents(@PathVariable UUID coachId) {
        List<Student> students = userService.getStudentsByCoachId(coachId);
        List<StudentDto> studentDtos = students.stream().map(student -> modelMapper.map(student, StudentDto.class)).toList();
        return new ResponseEntity<>(studentDtos, HttpStatus.OK);
    }

    @GetMapping("/coach/{coachId}/available")
    @Operation(
            summary = "Get students who can have an appointment"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Students fetched"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Students not found"
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
    public ResponseEntity<List<StudentDto>> getAvailableStudents(@PathVariable UUID coachId) {
        Map<Student, Long> students = userService.getAvailableStudents(coachId);
        List<StudentDto> studentDtos = new ArrayList<>();
        students.forEach((student, count) -> {
            StudentDto studentDto = modelMapper.map(student, StudentDto.class);
            studentDto.setNumOfClassCanBeScheduled(count);
            studentDtos.add(studentDto);
        });
        return new ResponseEntity<>(studentDtos, HttpStatus.OK);
    }

    @GetMapping("/{studentId}")
    @Operation(
            summary = "Get student by id"
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
    public ResponseEntity<StudentDto> getStudentById(@PathVariable UUID studentId) {
        Student student = userService.getStudentById(studentId);
        StudentDto studentDto = modelMapper.map(student, StudentDto.class);
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }
}
