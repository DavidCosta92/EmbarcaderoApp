package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftAddDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.user.UserReadDto;
import com.Embarcadero.demo.model.entities.Shift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShiftMapper {
    // TODO  @Autowired
    // TODO  RecordService recordService;
    @Autowired
    RecordMapper recordMapper;
    @Autowired
    UserMapper userMapper;

    public ShiftReadDto toReadDTO (Shift shift){
        List<RecordReadDto> recordReadDtos = shift.getRecords() == null ? new ArrayList<>() :  shift.getRecords().stream().map(record -> recordMapper.toReadDto(record)).toList() ;
        List<UserReadDto> userReadDtos =  shift.getStaff() == null ? new ArrayList<>() : shift.getStaff().stream().map(user -> userMapper.toReadDto(user)).toList() ;
        return new ShiftReadDto().builder()
                .id(shift.getId())
                .dam(shift.getDam())
                .date(shift.getDate())
                .records(recordReadDtos)
                .staff(userReadDtos)
                .notes(shift.getNotes())
                .build();
    }

    public Shift  toEntity (ShiftAddDto shiftAddDto){
        return new Shift().builder()
                .dam(shiftAddDto.getDam())
                .notes(shiftAddDto.getNotes())
                .build();
    }

}
