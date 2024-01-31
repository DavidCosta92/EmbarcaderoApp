package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.engine.EngineAddDto;
import com.Embarcadero.demo.model.dtos.engine.EngineReadDto;
import com.Embarcadero.demo.model.entities.Engine;
import com.Embarcadero.demo.model.mappers.EngineMapper;
import com.Embarcadero.demo.model.repositories.EngineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EngineService {
    @Autowired
    private EngineRepository engineRepository;

    @Autowired
    private EngineMapper engineMapper;

    public EngineReadDto addEngine(EngineAddDto engineAddDto){
        validateNewEngine(engineAddDto);
        engineRepository.save(engineMapper.addToEntity(engineAddDto));
        return findMotorByNumero_motor(engineAddDto.getEngineNumber());
    }
    public Engine getEngineByEngineNumber(String engineNumber){
        return engineRepository.findByEngineNumber(engineNumber);
    }
    public void validateNewEngine(EngineAddDto engineAddDto){
        if (engineRepository.existsByEngineNumber(engineAddDto.getEngineNumber())) throw new AlreadyExistException("Numero de motor ya existe");
    }

    public EngineReadDto findMotorByNumero_motor(String engineNumber){
        return engineMapper.entityToReadDto(getEngineByEngineNumber(engineNumber));
    }
}
