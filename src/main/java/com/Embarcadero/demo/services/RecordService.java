package com.Embarcadero.demo.services;

import com.Embarcadero.demo.model.dtos.boat.BoatReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import com.Embarcadero.demo.model.mappers.RecordMapper;
import com.Embarcadero.demo.model.repositories.RecordRepository;
import com.Embarcadero.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private Validator validator;

    @Autowired
    PersonService personService;
    @Autowired
    BoatService boatService;
    @Autowired

    public Record addNewRecord(RecordAddDto recordAddDto){
        if(recordAddDto.getNumberOfGuests() == null) recordAddDto.setNumberOfGuests(0);
        
        
        System.out.println("******************** notes "+recordAddDto.getNotes());


        if (recordAddDto.getNotes() == null) {
            recordAddDto.setNotes("Sin observaciones.");
        } else {
            // todo validator.stringOnlyLettersAndNumbers("Notas", recordAddDto.getNotes());
            // todo validator.stringOnlyLettersAndNumbers("Notas", recordAddDto.getNotes());

            recordAddDto.setNotes("Sin observaciones."); // FIX PROVISORIO

            // todo validator.stringOnlyLettersAndNumbers("Notas", recordAddDto.getNotes());
            // todo validator.stringOnlyLettersAndNumbers("Notas", recordAddDto.getNotes());
            // todo ESTE VALIDADOR NO FUNCIONA CORRECTAMENTE.. ME TOMA LOS ESPACIOS COMO ERRORES Y NO LOS PERMITE PASAR.. REVISAR!!!

        }
        if(recordAddDto.getNumberOfGuests() == null) recordAddDto.setNumberOfGuests(0);

        validator.stringOnlyLettersAndNumbers("Auto" , recordAddDto.getCar());


        if(recordAddDto.getHasLicense()){
            // todo ASUMO QUE BOTE YA EXISTE, YA QUE LAS LICENCIAS SOLO PUEDEN SER OTORGADAS EN SEDE NAUTICA
            recordAddDto.setBoat(boatService.findByName(recordAddDto.getBoat().getName()));
        }

        Record recordEntity = recordMapper.toEntity(recordAddDto);

        Person person = personService.getOrAddPersonForLicensesOrRecord(recordAddDto.getPerson());



        recordEntity.setPerson(person);
        Date startTime = new Date();
        recordEntity.setStartTime(startTime);;
        recordEntity.setRecordState(RecordState_enum.ACTIVO);

        return recordRepository.save(recordEntity);
    }

    public List<RecordReadDto> getOpenRecords(List<Record> records){
        Stream<Record> activeRecordsEntities = records.stream().filter(record -> record.getRecordState().equals("ACTIVO"));
        Stream<RecordReadDto> activeRecordsDtos = activeRecordsEntities.map(record -> recordMapper.toReadDto(record));
        return activeRecordsDtos.collect(Collectors.toList());
    }

}
