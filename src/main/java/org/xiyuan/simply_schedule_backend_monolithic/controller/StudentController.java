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
import org.xiyuan.simply_schedule_backend_monolithic.entity.Student;
import org.xiyuan.simply_schedule_backend_monolithic.payload.ErrorDto;
import org.xiyuan.simply_schedule_backend_monolithic.payload.StudentDto;
import org.xiyuan.simply_schedule_backend_monolithic.security.CurrentUser;
import org.xiyuan.simply_schedule_backend_monolithic.service.StudentService;

@RestController
@RequestMapping(path = "/api/v1/student", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD for students"
)
public class StudentController {
    private final StudentService studentService;
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
    public ResponseEntity<StudentDto> getStudent(@CurrentUser Student student) {
        StudentDto studentDtoResponse = modelMapper.map(student, StudentDto.class);
        return new ResponseEntity<>(studentDtoResponse, HttpStatus.OK);
    }
//    @GetMapping("/{email}")
//    @Operation(
//            summary = "Get student by email"
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Student fetched"
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Student not found"
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "HTTP Status Internal Server Error",
//                    content = @Content(
//                            schema = @Schema(implementation = ErrorDto.class)
//                    )
//            )
//    }
//    )
//    public ResponseEntity<StudentDto> getStudentByEmail(@PathVariable String email) {
//        Student student = studentService.getStudentByEmail(email);
//        StudentDto studentDtoResponse = modelMapper.map(student, StudentDto.class);
//        return new ResponseEntity<>(studentDtoResponse, HttpStatus.OK);
//    }
//
//    @PostMapping()
//    @Operation(
//            summary = "Create a student"
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "201",
//                    description = "Student created"
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "HTTP Status Internal Server Error",
//                    content = @Content(
//                            schema = @Schema(implementation = ErrorDto.class)
//                    )
//            )
//    }
//    )
//    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto StudentDto) {
//        Student Student = studentService.createStudent(modelMapper.map(StudentDto, Student.class));
//        StudentDto StudentDtoResponse = modelMapper.map(Student, StudentDto.class);
//        return new ResponseEntity<>(StudentDtoResponse, HttpStatus.CREATED);
//    }

//    @DeleteMapping("/{id}")
//    @Operation(
//            summary = "Delete an Student"
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Student deleted"
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Student not found"
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "HTTP Status Internal Server Error",
//                    content = @Content(
//                            schema = @Schema(implementation = ErrorDto.class)
//                    )
//            )
//    }
//    )
//    public ResponseEntity<String> deleteStudentById(@PathVariable Long id) {
//        studentService.deleteStudentById(id);
//        return new ResponseEntity<>("Deleted Student successfully", HttpStatus.OK);
//    }

}
