package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/")
public class RecordController {
    @Autowired
    private RecordService recordService;



    //TODO agregar un nuevo record?
    //TODO modificar un record?
    //TODO setear estado del record? ingreso, egreso, siniestraddo, eliminado ects..

}
