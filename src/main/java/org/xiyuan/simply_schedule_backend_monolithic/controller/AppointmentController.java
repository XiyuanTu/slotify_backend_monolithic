package org.xiyuan.simply_schedule_backend_monolithic.controller;

import org.xiyuan.simply_schedule_backend_monolithic.entity.Appointment;
import org.xiyuan.simply_schedule_backend_monolithic.payload.AppointmentDto;
import org.xiyuan.simply_schedule_backend_monolithic.payload.ErrorDto;
import org.xiyuan.simply_schedule_backend_monolithic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/appointment", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD for appointments"
)
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;

    @GetMapping("/{coachId}")
    @Operation(
            summary = "Get all the appointments of some coach"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointments fetched"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointments not found"
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
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByCoachId(@PathVariable Long coachId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByCoachId(coachId);
        List<AppointmentDto> appointmentDtos = appointments.stream().map(appointment -> modelMapper.map(appointment, AppointmentDto.class)).toList();
        // todo: combine with user dao
        return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
    }

    @PostMapping()
    @Operation(
            summary = "Create an appointment"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment created"
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
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.createAppointment(modelMapper.map(appointmentDto, Appointment.class));
        AppointmentDto appointmentDtoResponse = modelMapper.map(appointment, AppointmentDto.class);
        return new ResponseEntity<>(appointmentDtoResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an appointment"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
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
    public ResponseEntity<String> deleteAppointmentById(@PathVariable Long id) {
        appointmentService.deleteAppointmentById(id);
        return new ResponseEntity<>("Deleted appointment successfully", HttpStatus.OK);
    }

    @PutMapping()
    @Operation(
            summary = "Update an appointment"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
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
    public ResponseEntity<String> updateAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        appointmentService.updateAppointment(modelMapper.map(appointmentDto, Appointment.class));
        System.out.println(2);
        return new ResponseEntity<>("Updated appointment successfully", HttpStatus.OK);
    }
}
