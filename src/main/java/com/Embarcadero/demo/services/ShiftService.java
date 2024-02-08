package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import com.Embarcadero.demo.model.mappers.ShiftMapper;
import com.Embarcadero.demo.model.repositories.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ShiftService {
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    ShiftMapper shiftMapper;


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
    public ShiftReadDto findById (Integer id){
        Optional<Shift> shift = shiftRepository.findById(id);
        if(shift.isEmpty()) throw new NotFoundException("Turno no encontrado por id: "+id);
        return shiftMapper.toReadDTO(shift.get());
    }
}
