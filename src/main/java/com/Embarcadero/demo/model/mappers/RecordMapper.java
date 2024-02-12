package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDtoArray;
import com.Embarcadero.demo.model.entities.Boat;
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

    public Record toEntity (RecordAddDto addDto){
        Person p = personMapper.toEntity(addDto.getPerson());
        return new Record().builder()
                // .startTime(addDto.getStartTime())
                // .endTime(addDto.getEndTime())
                // .recordState(addDto.getRecordState())
                .boat(boatMapper.toEntity(addDto.getBoat()))
                .person(p)
                .numberOfGuests(addDto.getNumberOfGuests())
                .car(addDto.getCar())
                .notes(addDto.getNotes())
                .build();
    }

    public RecordReadDto toReadDto(Record record){
        return new RecordReadDto().builder()
                .id(record.getId())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .recordState(record.getRecordState())
                .boat(boatMapper.toReadDto(record.getBoat()))
                .person(personMapper.toReadDto(record.getPerson()))
                .numberOfGuests(record.getNumberOfGuests())
                .car(record.getCar())
                .notes(record.getNotes())
                .build();
    }

}
