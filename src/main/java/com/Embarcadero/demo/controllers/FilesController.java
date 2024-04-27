package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.auth.entities.AuthResponse;
import com.Embarcadero.demo.exceptions.ExceptionMessages;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.entities.ImageFile;
import com.Embarcadero.demo.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/v1/files/", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Files") // name of endpoint grup in swagger
@PreAuthorize("isAuthenticated()")
public class FilesController {

    @Autowired
    FileService fileService;
    @Operation(summary = "This endpoint receives an user ID, and a multipart file to set like user profile image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns name of file created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Exception.class)) })
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("user/profileImg/{idUser}")
    ResponseEntity<String> uploadUserProfileImg (@PathVariable String idUser, @RequestParam("file") MultipartFile file) throws IOException {
        // TODO deberia responder algun dto? Pero por el momento, solo un mensaje de todo ok
        return new ResponseEntity<>(fileService.uploadUserProfileImg(idUser, file), HttpStatus.CREATED);
    }

    @Operation(summary = "This endpoint receives an user ID, and returns de user profile image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns profile image",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Byte.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)) })
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("user/profileImg/{idUser}")
    ResponseEntity<byte[]> getUserProfileImg ( @PathVariable String idUser){
        ImageFile img = fileService.getUserProfileImg(idUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, img.getType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + img.getName()+"\"")
                .body(img.getData());
    }

    // todo endpoint para subir archivo excel

}
