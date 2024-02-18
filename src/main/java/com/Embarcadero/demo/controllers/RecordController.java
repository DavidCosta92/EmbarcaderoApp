package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordUpdateDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.services.RecordService;
import com.Embarcadero.demo.services.ShiftService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records/")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @Autowired
    private ShiftService shiftService;


    @PostMapping
    public ResponseEntity<ShiftReadDto> addNewRecord (@Valid @RequestBody RecordAddDto recordAddDTO){
        return new ResponseEntity<>(shiftService.addNewRecord(recordAddDTO) , HttpStatus.CREATED);
    }
    @PatchMapping("{id}")
    public ResponseEntity<RecordReadDto> updateRecord (@PathVariable Integer id, @RequestBody RecordUpdateDto recordUpdateDTO){
        return new ResponseEntity<>(shiftService.updateRecord(id,recordUpdateDTO) , HttpStatus.ACCEPTED);
    }
}
