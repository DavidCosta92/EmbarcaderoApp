package com.Embarcadero.demo.services;

import com.Embarcadero.demo.model.dtos.boat.BoatReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.entities.Boat;
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

    public RecordAddDto setDefaultValuesAddNewRecord(RecordAddDto recordAddDto){
        // este codigo podria salvarlo desde controlador, poniendo valores por default???
        if(recordAddDto.getNumberOfGuests() == null) recordAddDto.setNumberOfGuests(0);
        if (recordAddDto.getNotes() == null) {
            recordAddDto.setNotes("Sin observaciones.");
        } else {

            // todo ESTE VALIDADOR NO FUNCIONA CORRECTAMENTE.. ME TOMA LOS ESPACIOS COMO ERRORES Y NO LOS PERMITE PASAR.. REVISAR!!!
            // todo ESTE VALIDADOR NO FUNCIONA CORRECTAMENTE.. ME TOMA LOS ESPACIOS COMO ERRORES Y NO LOS PERMITE PASAR.. REVISAR!!!
            validator.stringText("Notas", recordAddDto.getNotes());
            // todo ESTE VALIDADOR NO FUNCIONA CORRECTAMENTE.. ME TOMA LOS ESPACIOS COMO ERRORES Y NO LOS PERMITE PASAR.. REVISAR!!!
            // todo ESTE VALIDADOR NO FUNCIONA CORRECTAMENTE.. ME TOMA LOS ESPACIOS COMO ERRORES Y NO LOS PERMITE PASAR.. REVISAR!!!

            recordAddDto.setNotes(recordAddDto.getNotes());
        }
        if(recordAddDto.getNumberOfGuests() == null) recordAddDto.setNumberOfGuests(0);
        Date startTime = new Date();
        recordAddDto.setStartTime(startTime);;
        recordAddDto.setRecordState(RecordState_enum.ACTIVO);
        return recordAddDto;
    }

    public Record addNewRecord(RecordAddDto recordAddDto){
        RecordAddDto addDto = setDefaultValuesAddNewRecord(recordAddDto); // setea los valores por defecto que no sean enviados, segun la logica de negocio

        validator.stringOnlyLettersAndNumbers("Auto" , addDto.getCar());

        if(addDto.getHasLicense()){ // ASUMO QUE BOTE YA EXISTE, YA QUE LAS LICENCIAS SOLO PUEDEN SER OTORGADAS EN SEDE NAUTICA
            BoatReadDto b = boatService.findByName(addDto.getBoat().getName());
            addDto.setBoat(b);
        }

        Record recordEntity = recordMapper.toEntity(addDto);

        Person person = personService.getOrAddPersonForLicensesOrRecord(addDto.getPerson());
        recordEntity.setPerson(person);

        return recordRepository.save(recordEntity);
    }

    public List<RecordReadDto> getOpenRecords(List<Record> records){
        Stream<Record> activeRecordsEntities = records.stream().filter(record -> record.getRecordState().equals("ACTIVO"));
        Stream<RecordReadDto> activeRecordsDtos = activeRecordsEntities.map(record -> recordMapper.toReadDto(record));
        return activeRecordsDtos.collect(Collectors.toList());
    }

}
