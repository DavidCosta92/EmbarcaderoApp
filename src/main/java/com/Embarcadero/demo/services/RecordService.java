package com.Embarcadero.demo.services;

import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDtoArray;
import com.Embarcadero.demo.model.dtos.records.RecordUpdateDto;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import com.Embarcadero.demo.model.mappers.BoatMapper;
import com.Embarcadero.demo.model.mappers.PersonMapper;
import com.Embarcadero.demo.model.mappers.RecordMapper;
import com.Embarcadero.demo.model.repositories.RecordRepository;
import com.Embarcadero.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
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
    PersonMapper personMapper;
    @Autowired
    BoatService boatService;
    @Autowired
    BoatMapper boatMapper;

    public Map<String,Integer> validateDate(String date){
        HashMap<String, Integer> resp = new HashMap<>();
        Integer year = null;
        Integer month = null;
        Integer day = null;

        if(date != null){
            String[] splitedDate = date.split("-");
            year = splitedDate.length>0 ? Integer.valueOf(splitedDate[0]) : null;
            month = splitedDate.length>1 ? Integer.valueOf(splitedDate[1]) : null;
            day = splitedDate.length>2 ? Integer.valueOf(splitedDate[2]) : null;
        }

        resp.put("y",year);
        resp.put("m",month);
        resp.put("d",day);
        return resp;
    }

    public RecordReadDtoArray findAllRecords(RecordState_enum recState, String sTime , String eTime, Integer page, Integer size, String sortBy){
        Map<String, Integer> startT = validateDate(sTime);
        Map<String, Integer> endT = validateDate(eTime);

        Page<Record> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        results = recordRepository.findAllByOptionalParameters(recState ,startT.get("y"), startT.get("m"), startT.get("d"), endT.get("y"), endT.get("m"), endT.get("d"), pageable);

        Page pagedResults = results.map(entity -> recordMapper.toReadDto(entity));
        return RecordReadDtoArray.builder()
                .records(pagedResults.getContent())
                .total_results(pagedResults.getTotalElements())
                .results_per_page(size)
                .current_page(page)
                .pages(pagedResults.getTotalPages())
                .sort_by(sortBy)
                .build();
    }
    public RecordAddDto setDefaultValuesAddNewRecord(RecordAddDto recordAddDto){
        if(recordAddDto.getNumberOfGuests() == null) recordAddDto.setNumberOfGuests(0);
        if (recordAddDto.getNotes() == null) {
            recordAddDto.setNotes("Sin observaciones.");
        } else {
            validator.stringText("Notas", recordAddDto.getNotes());
            recordAddDto.setNotes(recordAddDto.getNotes());
        }
        Date startTime = new Date();
        recordAddDto.setStartTime(startTime);;
        recordAddDto.setRecordState(RecordState_enum.ACTIVO);
        return recordAddDto;
    }
    public void changeRecordState (Record recordBd , RecordUpdateDto updateDto){
        recordBd.setRecordState(updateDto.getRecordState());

        if(!updateDto.getRecordState().equals(RecordState_enum.ACTIVO) && !updateDto.getRecordState().equals(RecordState_enum.DESCONOCIDO)){
            recordBd.setEndTime(new Date()); // si estado da baja al record, setear endtime con la fecha de baja
        } else {
            recordBd.setEndTime(null);// si estado da alta al record, eliminar endtime
        }
    }

    public RecordReadDto updateRecord(Record recordBd , RecordUpdateDto updateDto){
        // setear los datos nuevos
        if(updateDto.getRecordState()!= null) changeRecordState(recordBd,updateDto);

        if(updateDto.getNumberOfGuests()!= null) {
            validator.stringOnlyIntegerPositiveNumbers("Acompañantes", String.valueOf(updateDto.getNumberOfGuests()));
            recordBd.setNumberOfGuests(updateDto.getNumberOfGuests());
        }
        if(updateDto.getCar()!= null) {
            validateCar(updateDto.getCar());
            recordBd.setCar(updateDto.getCar());
        }
        if(updateDto.getNotes()!= null) recordBd.setNotes(updateDto.getNotes());

        if(updateDto.getBoat()!= null){
            recordBd.setBoat(boatService.getByName(updateDto.getBoat().getName()));// por regla negocio, solo oficinas pueden cambiar botes, solo se acepta bote si ya existe, sino getByName lanzara exception
        }
        if(updateDto.getPerson()!= null){
            recordBd.setPerson(personService.updateRecordPerson(recordBd, updateDto)); //Puede crear nueva persona si llegan todos los campos o editar si solo vienen algunos..
        }
        return recordMapper.toReadDto(recordRepository.save(recordBd));
    }
    public void validateCar(String car){
        validator.stringText("Auto" , car);
    }
    public Record addNewRecord(RecordAddDto recordAddDto){
        RecordAddDto addDto = setDefaultValuesAddNewRecord(recordAddDto); // setea los valores por defecto que no sean enviados, segun la logica de negocio
        validateCar(addDto.getCar());
        if(addDto.getHasLicense()) {
            addDto.setBoat(boatService.findByName(addDto.getBoat().getName())); // Las licencias solo las entrega nautica, por lo que si no existe, findByName, lanzara exception
        }
        Record recordEntity = recordMapper.toEntity(addDto);
        Person person = personService.getOrAddPersonForLicensesOrRecord(addDto.getPerson());
        recordEntity.setPerson(person);
        return recordRepository.save(recordEntity);
    }

    public List<RecordReadDto> getOpenRecords(List<Record> records){
        List<Record> activeRecordsEntities = records.stream().filter(record -> record.getRecordState().equals(RecordState_enum.ACTIVO)).collect(Collectors.toList());
        return activeRecordsEntities.stream().map(record -> recordMapper.toReadDto(record)).collect(Collectors.toList());
    }

}
