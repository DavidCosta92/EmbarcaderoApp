package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.engine.EngineAddDto;
import com.Embarcadero.demo.model.dtos.engine.EngineReadDto;
import com.Embarcadero.demo.model.dtos.engine.EngineUpdateDto;
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
        engineRepository.save(engineMapper.toEntity(engineAddDto));
        return findMotorByNumero_motor(engineAddDto.getEngineNumber());
    }
    public Engine getEngineByEngineNumber(String engineNumber){
        return engineRepository.findByEngineNumber(engineNumber);
    }
    public void validateNewEngine(EngineAddDto engineAddDto){
        if (engineRepository.existsByEngineNumber(engineAddDto.getEngineNumber())) throw new AlreadyExistException("Numero de motor ya existe");
    }

    public EngineReadDto findMotorByNumero_motor(String engineNumber){
        return engineMapper.toReadDto(getEngineByEngineNumber(engineNumber));
    }
    public Engine updateEngine(Engine engineBd , EngineUpdateDto engineUpdate){
        if(engineUpdate.getEngineType_enum() != null && engineUpdate.getEngineNumber() != null && engineUpdate.getCc() != null && engineUpdate.getNotes() != null){
            return engineMapper.toEntity(addEngine(engineMapper.toAddDto(engineUpdate))); // si vienen todos los datos, creo un engine nuevo y lo devuelvo
        } else{
            if (engineUpdate.getEngineType_enum() != null) {
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
                engineBd.setEngineType_enum(engineUpdate.getEngineType_enum());
            }
            if (engineUpdate.getEngineNumber() != null) {
                // TODO si viene, debo validar que no exista previamente el engine number
                // TODO si viene, debo validar que no exista previamente el engine number
                // TODO si viene, debo validar que no exista previamente el engine number
                // TODO si viene, debo validar que no exista previamente el engine number
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
                engineBd.setEngineNumber(engineUpdate.getEngineNumber());
            }
            if (engineUpdate.getCc() != null) {
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
                engineBd.setCc(engineUpdate.getCc());
            }
            if (engineUpdate.getNotes() != null) {
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
                engineBd.setNotes(engineUpdate.getNotes());
            }
            engineRepository.save(engineBd);
            return engineRepository.findByEngineNumber(engineBd.getEngineNumber());
        }
    }
}
