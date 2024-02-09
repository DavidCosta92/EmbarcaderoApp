package com.Embarcadero.demo.services;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.shift.ShiftAddDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.dtos.staff.StaffMemberAddDto;
import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import com.Embarcadero.demo.model.mappers.PersonMapper;
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

    public ShiftReadDto addStaffUser(Integer idShift, StaffMemberAddDto staffAddDto){
        Shift shiftBd = getShiftById(idShift);
        // TODO VALIDAR QUE NO EXISTA YA COMO MIEMBRO DEL STAFF...
        if(shiftBd.getStaff().stream().anyMatch(user -> user.getDni().equals(staffAddDto.getDni()))) throw new AlreadyExistException("Ya existe el usuario dentro del staff");
        User staffMemberEntity = userService.getUserStaffMemberByDni(staffAddDto.getDni());
        shiftBd.getStaff().add(staffMemberEntity);
        shiftRepository.save(shiftBd);
        return shiftMapper.toReadDTO(shiftBd);
    }

    public ShiftReadDto addStaffUser(Integer idShift, List<String> staffMemberDniList){
            // todo
            // todo
            // todo
            // todo
        return findById(idShift);
    }
}
