package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.model.dtos.license.LicenseReadDtoArray;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDtoArray;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/person/")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping("{dni}")
    public ResponseEntity<PersonReadDto> findByDni (@PathVariable String dni){
        return new ResponseEntity<>(personService.findPersonByDni(dni) , HttpStatus.ACCEPTED);
    }
    @GetMapping
    public ResponseEntity<PersonReadDtoArray> showAll(@RequestParam(required = false) String dni,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                                      @RequestParam(required = false, defaultValue = "dni") String sortBy) {
        return new ResponseEntity<>(personService.findAll(dni, page, size, sortBy), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<PersonReadDto> createPerson (@Valid @RequestBody PersonAddDto addDto){
        return new ResponseEntity<>(personService.createPerson(addDto) , HttpStatus.CREATED);
    }
    @PutMapping()
    public ResponseEntity<PersonReadDto> updatePerson (@RequestBody PersonUpdateDto updateDto){
        return new ResponseEntity<>(personService.updatePerson(updateDto) , HttpStatus.ACCEPTED);
    }
}
