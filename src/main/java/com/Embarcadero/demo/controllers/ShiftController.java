package com.Embarcadero.demo.controllers;

import
        com.Embarcadero.demo.exceptions.ExceptionMessages;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.shift.ShiftAddDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.dtos.shift.ShiftUpdateDto;
import com.Embarcadero.demo.model.dtos.staff.StaffMemberAddDto;
import com.Embarcadero.demo.model.dtos.user.UserStaffReadDto;
import com.Embarcadero.demo.model.entities.enums.Dam;
import com.Embarcadero.demo.services.ShiftService;
import com.Embarcadero.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;


@RestController
@RequestMapping(value = "/v1/shifts/", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Shifts") // name of endpoint grup in swagger
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasAnyRole('LIFEGUARD', 'ADMIN', 'SUPER_ADMIN')")
public class ShiftController {
    @Autowired
    ShiftService shiftService;

    @Autowired
    UserService userService;

    @Operation(summary = "This endpoint returns a pageable List of shifts, accepts search by dam, date, shiftState and user id. And sort by fields, on other hand for Paginated results gets size and page number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns all shifts",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDtoArray.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) })
    })
    @GetMapping
    public ResponseEntity<ShiftReadDtoArray> showAll (@RequestParam(required = false) Dam dam,
                                                      @RequestParam(required = false) String date,
                                                      @RequestParam(required = false) Boolean shiftState,
                                                      @RequestParam(required = false) Integer byUser,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                                      @RequestParam(required = false, defaultValue = "dam") String sortBy){
        return new ResponseEntity<>(shiftService.findAll(dam,date,shiftState,byUser, page, size, sortBy), HttpStatus.OK);
    }

    @Operation(summary = "This endpoint creates a new shift")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns created shift",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
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
    public ResponseEntity<ShiftReadDto> addNewShift(@Valid @RequestBody ShiftAddDto shiftAddDto){
        return new ResponseEntity<>(shiftService.createShift(shiftAddDto) , HttpStatus.CREATED);
    }

    @Operation(summary = "This endpoint return a shift by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a shift",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by Id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
    })
    @GetMapping("{id}")
    public ResponseEntity<ShiftReadDto> findById(@PathVariable Integer id){
        return new ResponseEntity<>(shiftService.findById(id) , HttpStatus.OK);
    }

    @Operation(summary = "This endpoint returns an active shift by id user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns an active shift that belongs to id user",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by Id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
    })
    @GetMapping("user/{id}")
    public ResponseEntity<ShiftReadDto> findShiftByIdUser(@PathVariable Integer id){
        return new ResponseEntity<>(shiftService.findShiftByIdUser(id) , HttpStatus.OK);
    }

    @Operation(summary = "This endpoint updates a shift")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns updated shift",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
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
    @PutMapping("{id}")
    public ResponseEntity<ShiftReadDto> updateShift (@PathVariable Integer id, @RequestBody ShiftUpdateDto shiftUpdateDto) throws JRException, IOException {
        return  new ResponseEntity<>(shiftService.updateShift(id , shiftUpdateDto ), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "This endpoint delete a shift by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns deleted license",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by Id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
    })
    @DeleteMapping("{id}")
    public ResponseEntity<ShiftReadDto> deleteShift (@PathVariable Integer id){
        return new ResponseEntity<>(shiftService.deleteShift(id) , HttpStatus.ACCEPTED);
    }

    @Operation(summary = "This endpoint add an staff to specific shift")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns shift with added staff",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "406", description = "Invalid input",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidValueException.class)) }),
            @ApiResponse(responseCode = "409", description = "Conflict, already reported data",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlreadyExistException.class)) }),
    })
    @PostMapping("{idShift}/staff/")
    public ResponseEntity<ShiftReadDto> addStaffToShift (@PathVariable Integer idShift , @Valid @RequestBody StaffMemberAddDto staffMemberDni){
        return new ResponseEntity<>(shiftService.addStaffUser(idShift , staffMemberDni), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "This endpoint returns a staff details by dni and staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns staff by dni",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserStaffReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)) }),
            @ApiResponse(responseCode = "406", description = "Invalid input",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidValueException.class)) }),
    })
    @GetMapping("{idShift}/staff/{dniUser}")
    public ResponseEntity<UserStaffReadDto> findUserStaffByDni (@PathVariable String dniUser){
        return new ResponseEntity<>(userService.findUserStaffByDni(dniUser), HttpStatus.OK);
    }

    @Operation(summary = "This endpoint deletes staff from shift")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns shift without staff",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)) }),
            @ApiResponse(responseCode = "406", description = "Invalid input",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidValueException.class)) }),
    })
    @PutMapping("{idShift}/staff/{idStaff}")
    public ResponseEntity<ShiftReadDto> removeStaffFromShift (@PathVariable Integer idShift , @PathVariable Integer idStaff){
        return new ResponseEntity<>(shiftService.removeStaffFromShift(idShift , idStaff), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "This endpoint generates a report and returns a byte array to be download")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a byte array",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)) })
    })
    @GetMapping("shiftResume/{idShift}")
    public ResponseEntity<byte[]> downloadShiftResume(@PathVariable Integer idShift) throws JRException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        LocalDate date = LocalDate.now();
        headers.setContentDispositionFormData("shiftReport", "Reporte guardia "+idShift+" -"+date+".pdf");
        return ResponseEntity.ok().headers(headers).body(shiftService.shiftResumePdf(idShift));
    }

    @Operation(summary = "This endpoint generates a report and to be send to provided email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a byte array",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)) })
    })
    @GetMapping("shiftResume/{idShift}/{email}")
    public ResponseEntity<Void> sendEmailShiftResume(@PathVariable Integer idShift, @PathVariable String email) throws JRException, IOException {
        if(shiftService.sendEmailShiftResume(idShift , email)){
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.internalServerError().build();
    }

}
