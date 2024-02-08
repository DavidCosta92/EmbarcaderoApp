package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.entities.Shift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShiftMapper {
    // TODO  @Autowired
    // TODO  RecordService recordService;
    @Autowired
    RecordMapper recordMapper;
    @Autowired
    PersonMapper personMapper;

    public ShiftReadDto toReadDTO (Shift shift){
        List<RecordReadDto> recordReadDtos = shift.getRecords().stream().map(record -> recordMapper.toReadDto(record)).toList();
        List<PersonReadDto> personReadDtos = shift.getStaff().stream().map(person -> personMapper.toReadDto(person)).toList();
        return new ShiftReadDto().builder()
                .id(shift.getId())
                .dam(shift.getDam())
                .date(shift.getDate())
                .records(recordReadDtos)
                .staff(personReadDtos)
                .build();
    }

}
