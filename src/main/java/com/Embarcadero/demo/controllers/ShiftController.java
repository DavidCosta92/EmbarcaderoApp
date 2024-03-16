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
                                                      @RequestParam(required = false) String date,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                                      @RequestParam(required = false, defaultValue = "dam") String sortBy){
        return new ResponseEntity<>(shiftService.findAll(dam,date, page, size, sortBy), HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<ShiftReadDto> findById(@PathVariable Integer id){
        return new ResponseEntity<>(shiftService.findById(id) , HttpStatus.OK);
    }
    @GetMapping("user/{id}")
    public ResponseEntity<ShiftReadDto> findShiftByIdUser(@PathVariable Integer id){
        return new ResponseEntity<>(shiftService.findShiftByIdUser(id) , HttpStatus.OK);
    }
    @PatchMapping("{id}")
    public ResponseEntity<ShiftReadDto> updateShift (@PathVariable Integer id, @RequestBody ShiftUpdateDto shiftUpdateDto){
        return  new ResponseEntity<>(shiftService.updateShift(id , shiftUpdateDto ), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ShiftReadDto> deleteShift (@PathVariable Integer id){
        return new ResponseEntity<>(shiftService.deleteShift(id) , HttpStatus.ACCEPTED);
    }

    @PostMapping("{idShift}/staff/")
    public ResponseEntity<ShiftReadDto> addStaffToShift (@PathVariable Integer idShift , @Valid @RequestBody StaffMemberAddDto staffMemberDni){
        return new ResponseEntity<>(shiftService.addStaffUser(idShift , staffMemberDni), HttpStatus.ACCEPTED);
    }
    @PatchMapping("{idShift}/staff/{idStaff}")
    public ResponseEntity<ShiftReadDto> removeStaffFromShift (@PathVariable Integer idShift , @PathVariable Integer idStaff){
        return new ResponseEntity<>(shiftService.removeStaffFromShift(idShift , idStaff), HttpStatus.ACCEPTED);
    }
}
