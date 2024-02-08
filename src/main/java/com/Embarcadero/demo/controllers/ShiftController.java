package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import com.Embarcadero.demo.services.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/shifts/")
public class ShiftController {
    @Autowired
    ShiftService shiftService;

    @GetMapping
    public ResponseEntity<ShiftReadDtoArray> showAll (@RequestParam(required = false) Dam_enum dam,
                                                      @RequestParam(required = false) Date date,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                                      @RequestParam(required = false, defaultValue = "dam") String sortBy){
        return new ResponseEntity<>(shiftService.findAll(dam,date, page, size, sortBy), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ShiftReadDto> findById(@PathVariable Integer id){
        return new ResponseEntity<>(shiftService.findById(id) , HttpStatus.OK);
    }



    // TODO SEGUIR CON EL RESTO DE ENDPOINTS!!!
    // TODO findByID, add, update, delete,





}
