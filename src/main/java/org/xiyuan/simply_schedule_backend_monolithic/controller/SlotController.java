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
import org.xiyuan.simply_schedule_backend_monolithic.entity.Slot;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Student;
import org.xiyuan.simply_schedule_backend_monolithic.payload.ErrorDto;
import org.xiyuan.simply_schedule_backend_monolithic.payload.SlotDto;
import org.xiyuan.simply_schedule_backend_monolithic.security.CurrentUser;
import org.xiyuan.simply_schedule_backend_monolithic.service.SlotService;

import java.util.List;

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

    @GetMapping("/{studentId}/{coachId}")
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
    public ResponseEntity<List<SlotDto>> getSlotsByStudentIdAndCoachId(@PathVariable Long studentId, @PathVariable Long coachId, @CurrentUser Student student) {
        List<Slot> slots = slotService.getSlotsByStudentIdAndCoachId(studentId, coachId);
        List<SlotDto> slotDtos = slots.stream().map(slot -> modelMapper.map(slot, SlotDto.class)).toList();
        // todo: combine with user dao
        return new ResponseEntity<>(slotDtos, HttpStatus.OK);
    }

    @GetMapping("/{coachId}")
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
    public ResponseEntity<List<SlotDto>> getSlotsByCoachId(@PathVariable Long coachId) {
        List<Slot> slots = slotService.getSlotsByCoachId(coachId);
        List<SlotDto> slotDtos = slots.stream().map(slot -> modelMapper.map(slot, SlotDto.class)).toList();
        // todo: combine with user dao
        return new ResponseEntity<>(slotDtos, HttpStatus.OK);
    }

    @PostMapping("/{studentId}/{coachId}")
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
    public ResponseEntity<List<SlotDto>> createSlots(@PathVariable Long studentId, @PathVariable Long coachId, @Valid @RequestBody List<SlotDto> slotDtos) {
        List<Slot> slots = slotDtos.stream().map(slotDto -> {
            slotDto.setStudentId(studentId);
            slotDto.setCoachId(coachId);
            return modelMapper.map(slotDto, Slot.class);
        }).toList();
        List<Slot> savedSlots = slotService.createSlots(studentId, coachId, slots);
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
    public ResponseEntity<String> deleteSlotById(@PathVariable Long id) {
        slotService.deleteSlotById(id);
        return new ResponseEntity<>("Deleted slot successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}/{coachId}")
    @Operation(
            summary = "Delete slots by studentId and coachId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Time slots deleted"
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
    public ResponseEntity<String> deleteSlotsByStudentIdAndCoachId(@PathVariable Long studentId, @PathVariable Long coachId) {
        slotService.deleteSlotsByStudentIdAndCoachId(studentId, coachId);
        return new ResponseEntity<>("Deleted slot successfully", HttpStatus.OK);
    }

    @PutMapping()
    @Operation(
            summary = "Update a time slot"
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
    public ResponseEntity<String> updateSlot(@Valid @RequestBody SlotDto slotDto) {
        slotService.updateSlot(modelMapper.map(slotDto, Slot.class));
        return new ResponseEntity<>("Updated slot successfully", HttpStatus.OK);
    }


}
