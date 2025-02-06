package org.xiyuan.simply_schedule_backend_monolithic.controller;

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
import org.xiyuan.simply_schedule_backend_monolithic.constant.SlotStatus;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.payload.ErrorDto;
import org.xiyuan.simply_schedule_backend_monolithic.payload.SlotDto;
import org.xiyuan.simply_schedule_backend_monolithic.service.SlotService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/slot", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD for time slots"
)
public class SlotController {
    private final SlotService slotService;
    private final ModelMapper modelMapper;

    @GetMapping("/student/{studentId}/coach/{coachId}")
    @Operation(
            summary = "Get all the time slots for some student and some coach"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Time slots fetched"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Time slot not found"
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
    public ResponseEntity<List<SlotDto>> getSlotsByStudentIdAndCoachId(@PathVariable UUID studentId, @PathVariable UUID coachId) {
        List<Slot> slots = slotService.getSlotsByStudentIdAndCoachId(studentId, coachId);
        List<SlotDto> slotDtos = slots.stream().map(slot -> modelMapper.map(slot, SlotDto.class)).toList();
        return new ResponseEntity<>(slotDtos, HttpStatus.OK);
    }

    @GetMapping("/coach/{coachId}")
    @Operation(
            summary = "Get all the time slots for some coach"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Time slots fetched"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Time slot not found"
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
    public ResponseEntity<List<SlotDto>> getSlotsByCoachId(@PathVariable UUID coachId) {
        List<Slot> slots = slotService.getSlotsByCoachId(coachId);
        List<SlotDto> slotDtos = slots.stream().map(slot -> modelMapper.map(slot, SlotDto.class)).toList();
        return new ResponseEntity<>(slotDtos, HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(
            summary = "Create time slots"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Time slots created"
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
    public ResponseEntity<List<SlotDto>> createSlots(@Valid @RequestBody List<SlotDto> slotDtos) {
        List<Slot> slots = slotDtos.stream().map(slotDto -> modelMapper.map(slotDto, Slot.class)).toList();
        List<Slot> savedSlots = slotService.createSlots(slots);
        List<SlotDto> slotDtoResponse = savedSlots.stream().map(slot -> modelMapper.map(slot, SlotDto.class)).toList();
        return new ResponseEntity<>(slotDtoResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a time slot"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Time slot deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Time slot not found"
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
    public ResponseEntity<String> deleteSlotById(@PathVariable UUID id) {
        slotService.deleteSlotById(id);
        return new ResponseEntity<>("Deleted slot successfully", HttpStatus.OK);
    }

//    @DeleteMapping("/student/{studentId}/coach/{coachId}")
//    @Operation(
//            summary = "Delete slots by studentId and coachId"
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Time slots deleted"
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
//    public ResponseEntity<String> deleteSlotsByStudentIdAndCoachId(@PathVariable UUID studentId, @PathVariable UUID coachId) {
//        slotService.deleteSlotsByStudentIdAndCoachId(studentId, coachId);
//        return new ResponseEntity<>("Deleted slot successfully", HttpStatus.OK);
//    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a time slot's status"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Time slot updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Time slot not found"
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
    public ResponseEntity<SlotDto> updateSlotStatus(@PathVariable UUID id, @RequestParam SlotStatus status) {
        Slot slot = slotService.updateSlotStatus(id, status);
        return new ResponseEntity<>(modelMapper.map(slot, SlotDto.class), HttpStatus.OK);
    }
}
