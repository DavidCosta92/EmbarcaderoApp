package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDtoArray;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.services.LicenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/licences/")
public class LicenseController {
    @Autowired
    private LicenseService licenseService;
    @PostMapping
    // @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> addLicence (@Valid @RequestBody LicenseAddDto licenseAddDto){
        return new ResponseEntity<>(licenseService.addLicense(licenseAddDto) , HttpStatus.CREATED);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDtoArray> showAll(@RequestParam(required = false) String licenseCode,
                                                       @RequestParam(required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(required = false, defaultValue = "10") Integer size,
                                                       @RequestParam(required = false, defaultValue = "licenseCode") String sortBy) {
        return new ResponseEntity<>(licenseService.findAll(licenseCode, page, size, sortBy), HttpStatus.OK);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> showById(@PathVariable Integer id) {
        return new ResponseEntity<>(licenseService.findById(id), HttpStatus.OK);
    }
    @GetMapping("licenseCode/{licenseCode}")
    @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> showByLicenseCode(@PathVariable String licenseCode) {
        return new ResponseEntity<>(licenseService.findByLicenseCode(licenseCode), HttpStatus.OK);
    }
    @PutMapping("{id}")
    // @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> updateById(@PathVariable Integer id , @RequestBody LicenseUpdateDto licenseUpdateDto) {
        return new ResponseEntity<>(licenseService.updateById(id , licenseUpdateDto), HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    // @PreAuthorize("hasAuthority('READ_ALL') OR isAnonymous()")
    public ResponseEntity<LicenseReadDto> deleteById(@PathVariable Integer id) {
        return new ResponseEntity<>(licenseService.deleteById(id), HttpStatus.OK);
    }


    // TODO ENDPOINT PARA SUMINISTRAR UN ARCHIVO EXCEL QUE LEVANTE TODOS LOS DATOS NECESARIOS DE MATRICULAS...


}
