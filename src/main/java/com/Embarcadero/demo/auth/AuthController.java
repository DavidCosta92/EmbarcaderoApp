package com.Embarcadero.demo.auth;

import com.Embarcadero.demo.auth.entities.*;
import com.Embarcadero.demo.exceptions.ExceptionMessages;
import com.Embarcadero.demo.model.dtos.user.UserReadDtoArray;
import com.Embarcadero.demo.model.dtos.user.UserUpdateDto;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor

public class AuthController {

    @Autowired
    private AuthService authService;

/*
    @Operation(summary = "This endpoint gets user data, register a new user and returns a JWT with credentials of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns JWT",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "406", description = "Error as result of sending invalid data, Ex: 'Password debe tener al menos 8 caracteres!' ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "409", description = "Error as result of sending data already reported, Ex: 'Datos ya existentes, revisa los campos!' ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }) })

 */
    @GetMapping("lifeguards")
    public ResponseEntity<UserReadDtoArray> getAllLifeguards (@RequestParam(required = false) String dni,
                                                              @RequestParam(required = false, defaultValue = "") String fullName,
                                                              @RequestParam(required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(required = false, defaultValue = "10") Integer size,
                                                              @RequestParam(required = false, defaultValue = "dni") String sortBy){
        return new ResponseEntity<>(authService.getAllLifeguards(dni,fullName, page, size, sortBy), HttpStatus.OK);
    }



    @Operation(summary = "This endpoint gets user data, register a new user and returns a JWT with credentials of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns JWT",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "406", description = "Error as result of sending invalid data, Ex: 'Password debe tener al menos 8 caracteres!' ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "409", description = "Error as result of sending data already reported, Ex: 'Datos ya existentes, revisa los campos!' ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }) })
    @PostMapping("register")
    public ResponseEntity<AuthResponse> register (@Valid @RequestBody RegisterRequest registerRequest){
        return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "This endpoint gets username and password and returns a JWT with credentials of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns JWT",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad credentials",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }) })
    @PostMapping("login")
    public ResponseEntity<AuthResponse> login (@Valid @RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @Operation(summary = "This endpoint gets a JWT, and returns an Object with User details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns an Object with User details",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoguedUserDetails.class)) }),
            @ApiResponse(responseCode = "403", description = "JWT not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "500", description = "ESTE ERROR ESTA PENDIENTE DE DARLE OTRO MANEJO!!! NO ESTA BIEN EL CODIGO 500 ",
                    content = @Content) })
    @GetMapping("/userDetails")
    public ResponseEntity<LoguedUserDetails> getLoguedUserDetails (@RequestHeader HttpHeaders headers){
        return new ResponseEntity<>(authService.getLoguedUserDetails(headers), HttpStatus.OK);
    }


    /*

    @Operation(summary = "This endpoint receives an RestorePassRequest as a JSON, SET NEW PASSWORD and returns a new JWT with user credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "JWT Invalid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "406", description = "Error as result of sending invalid data, Ex: 'Password debe tener al menos 8 caracteres!' ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "500", description = "ESTE ERROR ESTA PENDIENTE DE DARLE OTRO MANEJO!!! NO ESTA BIEN EL CODIGO 500 ",
                    content = @Content) })
    */
    @PutMapping(path = "userDetails")
    public ResponseEntity<LoguedUserDetails> editUserDetails (@RequestHeader HttpHeaders headers, @RequestBody UserUpdateDto userUpdateDto){
        return new ResponseEntity<>(authService.editUserDetails(headers, userUpdateDto), HttpStatus.ACCEPTED);
    }


    @Operation(summary = "This endpoint receives an email as a parametrer and if it belongs to a registered user, an email with JWT is sent to reset the password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Sent an email with JWT to reset the password.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Email Not Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "500", description = "ESTE ERROR ESTA PENDIENTE DE DARLE OTRO MANEJO!!! NO ESTA BIEN EL CODIGO 500 ",
                    content = @Content) })

    @GetMapping("restorePassword")
    public ResponseEntity<Boolean> restorePassword (@RequestParam @Email String email){
        return new ResponseEntity<>(authService.restorePassword(email), HttpStatus.ACCEPTED);
    }
    @Operation(summary = "This endpoint receives an RestorePassRequest as a JSON, SET NEW PASSWORD and returns a new JWT with user credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "JWT Invalid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "406", description = "Error as result of sending invalid data, Ex: 'Password debe tener al menos 8 caracteres!' ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessages.class)) }),
            @ApiResponse(responseCode = "500", description = "ESTE ERROR ESTA PENDIENTE DE DARLE OTRO MANEJO!!! NO ESTA BIEN EL CODIGO 500 ",
                    content = @Content) })
    @PostMapping(path = "setNewPassword") // AGREGAR PARA FORMS=> , consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<AuthResponse> setNewPassword(@RequestBody @Valid RestorePassRequest restorePassRequest){
        return new ResponseEntity<>(authService.setNewPassword(restorePassRequest) , HttpStatus.ACCEPTED);
    }

}