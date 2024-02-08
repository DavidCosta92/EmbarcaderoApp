package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDtoArray;
import com.Embarcadero.demo.model.entities.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecordMapper {
    @Autowired
    BoatMapper boatMapper;
    @Autowired
    PersonMapper personMapper;


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
