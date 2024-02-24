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
import com.Embarcadero.demo.model.mappers.ShiftMapper;
import com.Embarcadero.demo.model.repositories.ShiftRepository;
import com.Embarcadero.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftMapper shiftMapper;

    @Autowired
    private PersonService personService;
    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private Validator validator;


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
    public ShiftReadDtoArray findAll (Dam_enum dam, String stringDate, Integer page, Integer size, String sortBy){
        Page<Shift> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Map<String, Integer> date = validateDate(stringDate);

        results = shiftRepository.findAllByOptionalParameters(dam , date.get("y"), date.get("m"), date.get("d"), pageable);

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
    public ShiftReadDto findById (Integer id){
        return shiftMapper.toReadDTO(getShiftById(id));
    }

    public ShiftReadDto findShiftByIdUser (Integer id){
        return shiftMapper.toReadDTO(getShiftByIdUser(id));
    }
    public Shift getShiftByIdUser (Integer id){
        Optional<Shift> shift = shiftRepository.getShiftByIdUser(id);
        if(shift.isEmpty()) throw new NotFoundException("Turno no encontrado con el staff id: "+id);
        return shift.get();
    }

    public Shift getShiftById (Integer id){
        Optional<Shift> shift = shiftRepository.findById(id);
        if(shift.isEmpty()) throw new NotFoundException("Turno no encontrado por id: "+id);
        return shift.get();
    }
    public ShiftReadDto deleteShift (Integer id){
        Shift shiftToDelete = getShiftById(id);
        if (!shiftToDelete.getStaff().isEmpty()) throw new ForbiddenAction("Para eliminar turno, este no puede tener personal cargado!");
        List<RecordReadDto> openRecords = recordService.getOpenRecords(shiftToDelete.getRecords());
        if (!openRecords.isEmpty()) throw new ForbiddenAction("Para eliminar turno, este no puede tener registros abiertos!");
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
    public ShiftReadDto removeStaffFromShift(Integer idShift, Integer idStaff){
        Shift shiftBd = getShiftById(idShift);
        User userToRemove = userService.findById(idStaff);
        Boolean removeResult = shiftBd.getStaff().remove(userToRemove);
        if (!removeResult) throw new NotFoundException("No se encontro miembro de staff con id:"+idStaff);
        shiftRepository.save(shiftBd);
        return  shiftMapper.toReadDTO(shiftBd);
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

    public ShiftReadDto createShift(ShiftAddDto shiftAddDto){
        // si staff esta vacio, creo nueva lista.. sino, agrego listado
        if (shiftAddDto.getStaff() == null){
            shiftAddDto.setStaff(new ArrayList<>());
        } else{
            // todo agregar todas los users que envian al crear shift.. Util para un caso sin conexion, donde deberia recibir todos los datos juntos..
            // todo agregar todas los users que envian al crear shift.. Util para un caso sin conexion, donde deberia recibir todos los datos juntos..
            // todo agregar todas los users que envian al crear shift.. Util para un caso sin conexion, donde deberia recibir todos los datos juntos..
        }
        Shift shiftEntity = shiftMapper.toEntity(shiftAddDto);
        shiftEntity.setDate(LocalDate.now());
        shiftEntity.setClose(false);
        Shift savedShift = shiftRepository.save(shiftEntity);
        return shiftMapper.toReadDTO(savedShift);
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
        if (shiftUpdateDto.getClose() != null){
            setCloseStatusShift(shiftBd);
        }
        return shiftMapper.toReadDTO(shiftRepository.save(shiftBd));
    }
    public ShiftReadDto setCloseStatusShift(Shift shiftBd){
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

}
