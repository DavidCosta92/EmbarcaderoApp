package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.model.dtos.shift.ShiftAddDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.dtos.shift.ShiftUpdateDto;
import com.Embarcadero.demo.model.dtos.staff.StaffMemberAddDto;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import com.Embarcadero.demo.services.ShiftService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<ShiftReadDto> addNewShift(@Valid @RequestBody ShiftAddDto shiftAddDto){
        return new ResponseEntity<>(shiftService.createShift(shiftAddDto) , HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ShiftReadDtoArray> showAll (@RequestParam(required = false) Dam_enum dam,
                                                      @RequestParam(required = false) Date date,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                                      @RequestParam(required = false, defaultValue = "dam") String sortBy){
        return new ResponseEntity<>(shiftService.findAll(dam,date, page, size, sortBy), HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<ShiftReadDto> findById(@PathVariable Integer id){
        return new ResponseEntity<>(shiftService.findById(id) , HttpStatus.OK);
    }


    // TODO SEGUIR CON EL RESTO DE ENDPOINTS!!!
    // TODO update, delete,
    @PatchMapping("{id}")
    public ResponseEntity<ShiftReadDto> updateShift (@PathVariable Integer id, @RequestBody ShiftUpdateDto shiftUpdateDto){
        return  new ResponseEntity<>(shiftService.updateShift(id , shiftUpdateDto ), HttpStatus.ACCEPTED);
    }




    // TODO STAFF
    @PostMapping("{idShift}/staff/")
    public ResponseEntity<ShiftReadDto> addStaffToShift (@PathVariable Integer idShift , @Valid @RequestBody StaffMemberAddDto staffMemberDni){
        return new ResponseEntity<>(shiftService.addStaffUser(idShift , staffMemberDni), HttpStatus.ACCEPTED);
    }
}
