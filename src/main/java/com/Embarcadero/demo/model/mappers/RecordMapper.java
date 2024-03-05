package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecordMapper {
    @Autowired
    BoatMapper boatMapper;
    @Autowired
    PersonMapper personMapper;

    @Autowired
    LicenseMapper licenseMapper;
    public Record toEntity (RecordAddDto addDto){
        Person p = personMapper.toEntity(addDto.getPerson());
        return new Record().builder()
                .startTime(addDto.getStartTime())
                //.endTime(addDto.getEndTime())
                .recordState(addDto.getRecordState())
                // TODO ACA DEBO MODIFICAR COMPORTAMIENTO, QUE ACEPTE BOTES CON Y SIN MOTOR... ME TIRA EXCEPTION SI VIENE UN BOTE SIN LICENCIA.. ME VA A TIRAR ERROR SI VIENE UN BOTE CON LICENCIA Y SIN MOTOR, KAYAK DOBLE POR EJ..
                .simpleBoat(addDto.getSimpleBoat() !=null ? boatMapper.toEntity(addDto.getSimpleBoat()) : null)
                .license(addDto.getLicense() != null ? licenseMapper.toEntity(addDto.getLicense()) : null)
                .person(p)
                .numberOfGuests(addDto.getNumberOfGuests())
                .car(addDto.getCar())
                .notes(addDto.getNotes())
                .build();
    }
    public Record toEntity (RecordReadDto readDto){
        Person p = personMapper.toEntity(readDto.getPerson());
        return new Record().builder()
                .id(readDto.getId())
                // .startTime(readDto.getStartTime())
                // .endTime(readDto.getEndTime())
                // .recordState(readDto.getRecordState())
                .license(readDto.getLicense() != null ? readDto.getLicense() : null)
                .simpleBoat(readDto.getSimpleBoat() != null? boatMapper.toEntity(readDto.getSimpleBoat()) : null)
                .person(p)
                .numberOfGuests(readDto.getNumberOfGuests())
                .car(readDto.getCar())
                .notes(readDto.getNotes())
                .build();
    }


    public RecordReadDto toReadDto(Record record){
        return new RecordReadDto().builder()
                .id(record.getId())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .recordState(record.getRecordState())
                .simpleBoat(record.getSimpleBoat() != null ? boatMapper.toReadDto(record.getSimpleBoat()) : null)
                .license(record.getLicense() != null ? record.getLicense() : null)
                .person(personMapper.toReadDto(record.getPerson()))
                .numberOfGuests(record.getNumberOfGuests())
                .car(record.getCar())
                .notes(record.getNotes())
                .build();
    }

}
