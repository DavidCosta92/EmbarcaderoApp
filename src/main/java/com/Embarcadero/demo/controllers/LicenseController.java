package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.exceptions.ExceptionMessages;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDtoArray;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.services.LicenseService;
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
@RequestMapping("/v1/licences/")
@Tag(name = "Licences")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("isAuthenticated()")
public class LicenseController {
    @Autowired
    private LicenseService licenseService;

    @Operation(summary = "This endpoint creates a new license")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns license created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LicenseReadDto.class)) }),
            @ApiResponse(responseCode = "406", description = "Invalid input",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = InvalidValueException.class)) }),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AlreadyExistException.class)) })
    })
    @PreAuthorize("hasAnyRole('OFFICE', 'ADMIN', 'SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<LicenseReadDto> addLicence (@Valid @RequestBody LicenseAddDto licenseAddDto){
        return new ResponseEntity<>(licenseService.addLicense(licenseAddDto) , HttpStatus.CREATED);
    }

    @Operation(summary = "This endpoint returns a pageable List of users, accepts search by code, name, lastname, and boat name. And sort by fields, on other hand for Paginated results gets size and page number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all licences",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LicenseReadDtoArray.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) })
    })
    @GetMapping
    public ResponseEntity<LicenseReadDtoArray> showAll(@RequestParam(required = false) String code,
                                                       @RequestParam(required = false) String searchValue,
                                                       @RequestParam(required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(required = false, defaultValue = "10") Integer size,
                                                       @RequestParam(required = false, defaultValue = "code") String sortBy) {
        return new ResponseEntity<>(licenseService.findAll(code, searchValue, page, size, sortBy), HttpStatus.OK);
    }

    @Operation(summary = "This endpoint returns license by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns license by Id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LicenseReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by Id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) })
    })
    @GetMapping("{id}")
    public ResponseEntity<LicenseReadDto> showById (@PathVariable Integer id) {
        return new ResponseEntity<>(licenseService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "This endpoint returns license by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns license by Id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LicenseReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by Id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) })
    })
    @GetMapping("code/{code}")
    public ResponseEntity<LicenseReadDto> showByCode(@PathVariable String code) {
        return new ResponseEntity<>(licenseService.findByCode(code), HttpStatus.OK);
    }

    @Operation(summary = "This endpoint gets an Id, and data to update license. It is capable to receive one or more fields to update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns updated license",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LicenseReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by Id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "406", description = "Invalid inputs",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidValueException.class)) })
    })
    @PreAuthorize("hasAnyRole('OFFICE', 'ADMIN', 'SUPER_ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<LicenseReadDto> updateById(@PathVariable Integer id , @RequestBody LicenseUpdateDto licenseUpdateDto) {
        return new ResponseEntity<>(licenseService.updateById(id , licenseUpdateDto), HttpStatus.OK);
    }

    @Operation(summary = "This endpoint delete a license by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns deleted license",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LicenseReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by Id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
    })
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OFFICE', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<LicenseReadDto> deleteById(@PathVariable Integer id) {
        return new ResponseEntity<>(licenseService.deleteById(id), HttpStatus.OK);
    }

    // TODO ENDPOINT PARA SUMINISTRAR UN ARCHIVO EXCEL QUE LEVANTE TODOS LOS DATOS NECESARIOS DE MATRICULAS...
}
