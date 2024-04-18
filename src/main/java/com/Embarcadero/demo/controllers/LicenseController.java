package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.exceptions.ExceptionMessages;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/licences/")
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
    @PostMapping
    // @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> addLicence (@Valid @RequestBody LicenseAddDto licenseAddDto){
        return new ResponseEntity<>(licenseService.addLicense(licenseAddDto) , HttpStatus.CREATED);
    }

    @Operation(summary = "This endpoint returns a pageable List of users, accepts search by licenseCode, name, lastname, and boat name. And sort by fields, on other hand for Paginated results gets size and page number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all licences",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LicenseReadDtoArray.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) })
    })
    @GetMapping
    // @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDtoArray> showAll(@RequestParam(required = false) String licenseCode,
                                                       @RequestParam(required = false) String searchValue,
                                                       @RequestParam(required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(required = false, defaultValue = "10") Integer size,
                                                       @RequestParam(required = false, defaultValue = "licenseCode") String sortBy) {
        return new ResponseEntity<>(licenseService.findAll(licenseCode, searchValue, page, size, sortBy), HttpStatus.OK);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> showById(@PathVariable Integer id) {
        return new ResponseEntity<>(licenseService.findById(id), HttpStatus.OK);
    }
    @GetMapping("licenseCode/{licenseCode}")
    @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> showByLicenseCode(@PathVariable String licenseCode) {
        return new ResponseEntity<>(licenseService.findByLicenseCode(licenseCode), HttpStatus.OK);
    }
    @PutMapping("{id}")
    // @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> updateById(@PathVariable Integer id , @RequestBody LicenseUpdateDto licenseUpdateDto) {
        return new ResponseEntity<>(licenseService.updateById(id , licenseUpdateDto), HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    // @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> deleteById(@PathVariable Integer id) {
        return new ResponseEntity<>(licenseService.deleteById(id), HttpStatus.OK);
    }


    // TODO ENDPOINT PARA SUMINISTRAR UN ARCHIVO EXCEL QUE LEVANTE TODOS LOS DATOS NECESARIOS DE MATRICULAS...


}
