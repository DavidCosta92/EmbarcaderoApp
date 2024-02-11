package com.Embarcadero.demo.services;

import com.Embarcadero.demo.model.dtos.boat.BoatReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import com.Embarcadero.demo.model.mappers.RecordMapper;
import com.Embarcadero.demo.model.repositories.RecordRepository;
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


    public Record addNewRecord(RecordAddDto recordAddDto){
        // todo validar record en si
        // todo validar record en si

        // Validado por clases numberOfGuests,car,notes; idShift validado por shift
        // NO SE RECIBEN startTime endTime recordState;



        if(recordAddDto.getHasLicense()){
            // todo validar BoatReadDto boat;
            // todo validar BoatReadDto boat;
            // todo validar BoatReadDto boat;
        }


        // todo validar PersonReadDto person;
        // PERSONA PUEDE O NO EXISTIR ANTES.. DEBERIA HACER UN personService.getOrAddPerosna(recordAddDto=
        // todo validar PersonReadDto person;

        // todo validar record en si
        // todo validar record en si
        // todo validar record en si

        Record recordEntity = recordMapper.toEntity(recordAddDto);

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
