package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.exceptions.ExceptionMessages;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDtoArray;
import com.Embarcadero.demo.model.dtos.records.RecordUpdateDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.entities.enums.RecordState;
import com.Embarcadero.demo.services.RecordService;
import com.Embarcadero.demo.services.ShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/records/")
@Tag(name = "Records") // name of endpoint grup in swagger
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("isAuthenticated() AND hasAnyRole('LIFEGUARD', 'ADMIN', 'SUPER_ADMIN')")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @Autowired
    private ShiftService shiftService;

    @Operation(summary = "This endpoint returns a pageable List of records, accepts search by recordState, startTime or endTime. And sort by fields, on other hand for Paginated results gets size and page number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all records",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RecordReadDtoArray.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) })
    })
    @PreAuthorize("hasAnyRole('LIFEGUARD', 'ADMIN', 'SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<RecordReadDtoArray> findAllRecords (@RequestParam(required = false) RecordState recordState,
                                                              @RequestParam(required = false) String startTime,
                                                              @RequestParam(required = false) String endTime,
                                                              @RequestParam(required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(required = false, defaultValue = "10") Integer size,
                                                              @RequestParam(required = false, defaultValue = "recordState") String sortBy){
        return new ResponseEntity<>(recordService.findAllRecords(recordState, startTime , endTime, page, size, sortBy), HttpStatus.OK);
    }

    @Operation(summary = "This endpoint creates a new record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns shift where record was added",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials or shift is already closed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "406", description = "Invalid input",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidValueException.class)) }),
            @ApiResponse(responseCode = "409", description = "Conflict, already reported data",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlreadyExistException.class)) }),
    })
    @PostMapping
    public ResponseEntity<ShiftReadDto> addNewRecord (@Valid @RequestBody RecordAddDto recordAddDTO){
        return new ResponseEntity<>(shiftService.addNewRecord(recordAddDTO) , HttpStatus.CREATED);
    }

    @Operation(summary = "This endpoint updates a record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns updated person",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RecordReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)) }),
            @ApiResponse(responseCode = "406", description = "Invalid input",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidValueException.class)) }),
            @ApiResponse(responseCode = "409", description = "Conflict, already reported data",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlreadyExistException.class)) }),
    })
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.PATCH})
    @PutMapping("{id}")
    public ResponseEntity<RecordReadDto> updateRecord (@PathVariable Integer id, @RequestBody RecordUpdateDto recordUpdateDTO){
        return new ResponseEntity<>(shiftService.updateRecord(id,recordUpdateDTO) , HttpStatus.ACCEPTED);
    }

    @Operation(summary = "This endpoint gets a record by dni")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a person",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RecordReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
    })
    @GetMapping("{id}")
    public ResponseEntity<RecordReadDto> findById (@PathVariable Integer id){
        return new ResponseEntity<>(recordService.findById(id) , HttpStatus.ACCEPTED);
    }
}

