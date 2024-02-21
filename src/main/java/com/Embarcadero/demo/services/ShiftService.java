package com.Embarcadero.demo.services;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.ForbiddenAction;
import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordUpdateDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftAddDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.dtos.shift.ShiftUpdateDto;
import com.Embarcadero.demo.model.dtos.staff.StaffMemberAddDto;
import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import com.Embarcadero.demo.model.mappers.PersonMapper;
import com.Embarcadero.demo.model.mappers.RecordMapper;
import com.Embarcadero.demo.model.mappers.ShiftMapper;
import com.Embarcadero.demo.model.repositories.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftMapper shiftMapper;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;


    public ShiftReadDtoArray findAll (Dam_enum dam, Date date, Integer page, Integer size, String sortBy){
        Page<Shift> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        if( dam !=null && date == null){
            results = shiftRepository.findAllByDamContains(dam, pageable);
        } else if( dam == null && date!= null){
            results = shiftRepository.findAllByDateContains(date, pageable);
        } else if( dam != null && date!= null){
            results = shiftRepository.findAllByDamContainsAndDateContains(dam , date, pageable);
        } else {
            results = shiftRepository.findAll(pageable);
        }

        Page pagedResults = results.map(entity -> shiftMapper.toReadDTO(entity));
        return ShiftReadDtoArray.builder()
                .shifts(pagedResults.getContent())
                .total_results(pagedResults.getTotalElements())
                .results_per_page(size)
                .current_page(page)
                .pages(pagedResults.getTotalPages())
                .sort_by(sortBy)
                .build();
    }

    public ShiftReadDto createShift(ShiftAddDto shiftAddDto){
        // todo validar DAM
        // TODO VALIDAR NOTES

        // si staff esta vacio, creo nueva lista.. sino, agrego listado
        if (shiftAddDto.getStaff() == null){
            shiftAddDto.setStaff(new ArrayList<>());
        } else{
            // todo agregar todas los users del staff
            // todo agregar todas los users del staff
            // todo agregar todas los users del staff
        }

        Shift shiftEntity = shiftMapper.toEntity(shiftAddDto);

        // todo Date currentDate = new Date();

        shiftEntity.setDate(LocalDate.now());

        Shift savedShift = shiftRepository.save(shiftEntity);
        return shiftMapper.toReadDTO(savedShift);
    }

    public ShiftReadDto findById (Integer id){
        return shiftMapper.toReadDTO(getShiftById(id));
    }
    public Shift getShiftById (Integer id){
        Optional<Shift> shift = shiftRepository.findById(id);
        if(shift.isEmpty()) throw new NotFoundException("Turno no encontrado por id: "+id);
        return shift.get();
    }

    public ShiftReadDto updateShift (Integer idShift, ShiftUpdateDto shiftUpdateDto){
        Shift  shiftBd = getShiftById(idShift);
        if (shiftUpdateDto.getDam() != null){
            // todo validar dam? dam es validada automaticamente por spring, pero deberia validar algo mas? como que solo puede haber un unico uso de la dam en un dia particular?
            shiftBd.setDam(shiftUpdateDto.getDam());
        }
        if (shiftUpdateDto.getNotes() != null){
            // todo validar notes
            shiftBd.setNotes(shiftUpdateDto.getNotes());
        }
        System.out.println("<<<<<<<<<<< shiftBd getClose => "+shiftBd.getClose());
        if (shiftUpdateDto.getClose() != null){
            closeShift(shiftBd);
        }
        return shiftMapper.toReadDTO(shiftRepository.save(shiftBd));
    }
    public ShiftReadDto closeShift (Shift shiftBd){
        if (recordService.getOpenRecords(shiftBd.getRecords()).size() > 0){
            throw new ForbiddenAction("Aun existen registros activos, por favor cierra todos los registros!");
        }
        shiftBd.setClose(shiftBd.getClose() == true? false : true);
        shiftRepository.save(shiftBd);
        // todo ENVIAR EMAIL con resumen de shift (fecha, staff, records, notas)
        // todo ENVIAR EMAIL con resumen de shift (fecha, staff, records, notas)
        // todo ENVIAR EMAIL con resumen de shift (fecha, staff, records, notas)
        // todo ENVIAR EMAIL con resumen de shift (fecha, staff, records, notas)
        // todo ENVIAR EMAIL con resumen de shift (fecha, staff, records, notas)
        // todo ENVIAR EMAIL con resumen de shift (fecha, staff, records, notas)
        // todo ENVIAR EMAIL con resumen de shift (fecha, staff, records, notas)
        return shiftMapper.toReadDTO(shiftBd);
    }

    public ShiftReadDto deleteShift (Integer id){
        Shift shiftToDelete = getShiftById(id);
        shiftRepository.deleteById(id);
        return shiftMapper.toReadDTO(shiftToDelete);
    }
    public ShiftReadDto addStaffUser(Integer idShift, StaffMemberAddDto staffAddDto){
        Shift shiftBd = getShiftById(idShift);
        if(shiftBd.getStaff().stream().anyMatch(user -> user.getDni().equals(staffAddDto.getDni()))) throw new AlreadyExistException("Ya existe el usuario dentro del staff");
        User staffMemberEntity = userService.getUserStaffMemberByDni(staffAddDto.getDni());
        shiftBd.getStaff().add(staffMemberEntity);
        shiftRepository.save(shiftBd);
        return shiftMapper.toReadDTO(shiftBd);
    }

    public ShiftReadDto addNewRecord(RecordAddDto recordAddDTO){
        // validar que shift exista y que este en condiciones de agregar registro
        Shift shiftBd = getShiftById(recordAddDTO.getIdShift());
        if (shiftBd.getClose()) throw new ForbiddenAction("La guardia esta cerrada, es imposible agregar el registro!");

        // obtener registros, verificar que no exista y crear nuevo record, agregarlo y actualizar shift...
        List<Record> records = shiftBd.getRecords();
        if(!records.isEmpty()) validateNonDuplicatedRecords(records, recordAddDTO);  // validar que no hayan registros duplicados... mediante nombre embarcacion
        Record newRecord = recordService.addNewRecord(recordAddDTO);

        records.add(newRecord);
        shiftBd.setRecords(records);
        return shiftMapper.toReadDTO(shiftRepository.save(shiftBd));
    }

    public RecordReadDto updateRecord(Integer idRecord, RecordUpdateDto updateDto){
        Shift shiftBd = getShiftById(updateDto.getIdShift()); // obtener shift en bd
        List<Record> recordBdList = shiftBd.getRecords().stream().filter(record -> record.getId() == idRecord).collect(Collectors.toList());
        if (recordBdList.isEmpty()) throw new NotFoundException("El registro a actualzar con id: "+idRecord+", no existe en el turno id: "+updateDto.getIdShift()+", verifica los datos!");
        return recordService.updateRecord(recordBdList.get(0) ,updateDto);
    }

    public void validateNonDuplicatedRecords (List<Record> records , RecordAddDto recordAddDTO){
        List<Record> activeRecords = records.stream().filter(rec -> rec.getRecordState().equals(RecordState_enum.ACTIVO)).collect(Collectors.toList());
        if (!activeRecords.isEmpty()){ // si hay registros activos
            if (recordAddDTO.getHasLicense()){ // si tiene licencia, verificar si existe algun record activo con un bote llamado igual al record..
                if (activeRecords.stream().anyMatch(rec -> rec.getBoat().getName().equals(recordAddDTO.getBoat().getName()))) throw new InvalidValueException("Ya existe un registro con una embarcacion llamada " + recordAddDTO.getBoat().getName());
            } else{
                // todo si no tiene licencia, verificar que entre los registros activos no hay un auto con la misma patente.. en dicho caso deberia pedir que agregue mas de una embarcacion..
            }
        }

    }

    public ShiftReadDto addStaffUser(Integer idShift, List<String> staffMemberDniList){
            // todo ?? ES UTIL?
        return findById(idShift);
    }
}
