package com.Embarcadero.demo.services;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.ForbiddenAction;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftAddDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.dtos.shift.ShiftUpdateDto;
import com.Embarcadero.demo.model.dtos.staff.StaffMemberAddDto;
import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Date currentDate = new Date();
        shiftEntity.setDate(currentDate);

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

        // crear nuevo record, agregarlo y actualizar shift...
        List<Record> records = shiftBd.getRecords();
        Record newRecord = recordService.addNewRecord(recordAddDTO);
        records.add(newRecord);
        shiftBd.setRecords(records);
        return shiftMapper.toReadDTO(shiftRepository.save(shiftBd));
    }

    public ShiftReadDto addStaffUser(Integer idShift, List<String> staffMemberDniList){
            // todo ?? ES UTIL?
        return findById(idShift);
    }
}
