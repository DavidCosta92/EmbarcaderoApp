package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.model.dtos.MatriculasArrayDto;
import com.Embarcadero.demo.services.MatriculasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matriculas/")
public class MatriculaController {
    @Autowired
    private MatriculasService matriculasService;


    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<MatriculasArrayDto> showAll( @RequestParam(required = false) String matricula,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                                      @RequestParam(required = false, defaultValue = "matricula") String sortBy) {
        return new ResponseEntity<>(matriculasService.findAll(matricula, page, size, sortBy), HttpStatus.OK);
    }
}
