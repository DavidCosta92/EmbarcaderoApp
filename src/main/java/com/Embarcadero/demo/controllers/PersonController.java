package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.exceptions.ExceptionMessages;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDtoArray;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDtoArray;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.services.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/person/")
@Tag(name = "Persons") // name of endpoint grup in swagger
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("isAuthenticated() AND hasAnyRole('OFFICE','LIFEGUARD', 'ADMIN', 'SUPER_ADMIN')")
public class PersonController {
    @Autowired
    PersonService personService;

    @Operation(summary = "This endpoint gets a person by dni")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a person",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PersonReadDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found by dni",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
    })
    @GetMapping("{dni}")
    public ResponseEntity<PersonReadDto> findByDni (@PathVariable String dni){
        return new ResponseEntity<>(personService.findPersonByDni(dni) , HttpStatus.OK);
    }

    @Operation(summary = "This endpoint returns a pageable List of persons, accepts search by name, lastName or dni. And sort by fields, on other hand for Paginated results gets size and page number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all persons",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PersonReadDtoArray.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) })
    })
    @GetMapping
    public ResponseEntity<PersonReadDtoArray> showAll(@RequestParam(required = false) String dni,
                                                      @RequestParam(required = false, defaultValue = "") String searchValue,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                                      @RequestParam(required = false, defaultValue = "dni") String sortBy) {
        return new ResponseEntity<>(personService.findAll(dni,searchValue, page, size, sortBy), HttpStatus.OK);
    }

    @Operation(summary = "This endpoint creates a new person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns created person",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PersonReadDto.class)) }),
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
    @PostMapping()
    public ResponseEntity<PersonReadDto> createPerson (@Valid @RequestBody PersonAddDto addDto){
        return new ResponseEntity<>(personService.createPerson(addDto) , HttpStatus.CREATED);
    }

    @Operation(summary = "This endpoint updates a person, and requires dni of person and any field to update, Note: dni cant be updated!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Returns updated person",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PersonReadDto.class)) }),
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
    @PutMapping()
    public ResponseEntity<PersonReadDto> updatePerson (@RequestBody PersonUpdateDto updateDto){
        return new ResponseEntity<>(personService.updatePerson(updateDto) , HttpStatus.ACCEPTED);
    }
}
