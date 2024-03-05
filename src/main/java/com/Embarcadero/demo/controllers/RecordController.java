package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDtoArray;
import com.Embarcadero.demo.model.dtos.records.RecordUpdateDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import com.Embarcadero.demo.services.RecordService;
import com.Embarcadero.demo.services.ShiftService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.PATCH})
    @PutMapping("{id}")
    public ResponseEntity<RecordReadDto> updateRecord (@PathVariable Integer id, @RequestBody RecordUpdateDto recordUpdateDTO){
        return new ResponseEntity<>(shiftService.updateRecord(id,recordUpdateDTO) , HttpStatus.ACCEPTED);
    }
    @GetMapping("{id}")
    public ResponseEntity<RecordReadDto> findById (@PathVariable Integer id){
        return new ResponseEntity<>(recordService.findById(id) , HttpStatus.ACCEPTED);
    }
    @GetMapping
    public ResponseEntity<RecordReadDtoArray> findAllRecords (@RequestParam(required = false) RecordState_enum recordState,
                                                             @RequestParam(required = false) String startTime,
                                                             @RequestParam(required = false) String endTime,
                                                             @RequestParam(required = false, defaultValue = "0") Integer page,
                                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                                             @RequestParam(required = false, defaultValue = "recordState") String sortBy){
        return new ResponseEntity<>(recordService.findAllRecords(recordState, startTime , endTime, page, size, sortBy), HttpStatus.OK);
    }


}

