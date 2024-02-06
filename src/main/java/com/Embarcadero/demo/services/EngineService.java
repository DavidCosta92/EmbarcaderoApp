package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.engine.EngineAddDto;
import com.Embarcadero.demo.model.dtos.engine.EngineReadDto;
import com.Embarcadero.demo.model.dtos.engine.EngineUpdateDto;
import com.Embarcadero.demo.model.entities.Engine;
import com.Embarcadero.demo.model.entities.enums.EngineType_enum;
import com.Embarcadero.demo.model.mappers.EngineMapper;
import com.Embarcadero.demo.model.repositories.EngineRepository;
import com.Embarcadero.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EngineService {
    @Autowired
    private EngineRepository engineRepository;

    @Autowired
    private EngineMapper engineMapper;
    @Autowired
    private Validator validator;

    public EngineReadDto addEngine(EngineAddDto engineAddDto){
        validateNewEngine(engineAddDto);
        engineRepository.save(engineMapper.toEntity(engineAddDto));
        return findMotorByNumero_motor(engineAddDto.getEngineNumber());
    }
    public Engine getEngineByEngineNumber(String engineNumber){
        return engineRepository.findByEngineNumber(engineNumber);
    }
    public void validateNewEngine(EngineAddDto engineAddDto){
        validateCc(engineAddDto.getCc());
        validateEngineNumber( engineAddDto.getEngineNumber());
    }

    public EngineReadDto findMotorByNumero_motor(String engineNumber){
        validator.stringOnlyLettersAndNumbers("Numero de motor", engineNumber);
        return engineMapper.toReadDto(getEngineByEngineNumber(engineNumber));
    }
    public void validateEngineNumber(String engineNumber){
        validator.stringOnlyLettersAndNumbers("Numero de motor", engineNumber);
        if(engineRepository.existsByEngineNumber(engineNumber)) throw new AlreadyExistException("Numero de motor ya existe");
    }
    public void validateCc(String cc){
        validator.stringMinSize("Cilindradas", 2 ,cc);
        validator.stringOnlyIntegerPositiveNumbers("Cilindradas", cc);
    }
    public Engine updateEngine(Engine engineBd , EngineUpdateDto engineUpdate){
        if(engineUpdate.getEngineType_enum() != null && engineUpdate.getEngineNumber() != null && engineUpdate.getCc() != null && engineUpdate.getNotes() != null){
            return engineMapper.toEntity(addEngine(engineMapper.toAddDto(engineUpdate))); // si vienen todos los datos, creo un engine nuevo y lo devuelvo
        } else{
            if (engineUpdate.getEngineType_enum() != null) {
                engineBd.setEngineType_enum(engineUpdate.getEngineType_enum());
            }
            if (engineUpdate.getEngineNumber() != null) {
                validateEngineNumber( engineUpdate.getEngineNumber());
                engineBd.setEngineNumber(engineUpdate.getEngineNumber());
            }
            if (engineUpdate.getCc() != null) {
                validateCc(engineUpdate.getCc());
                engineBd.setCc(engineUpdate.getCc());
            }
            if (engineUpdate.getNotes() != null) {
                engineBd.setNotes(engineUpdate.getNotes());
            }
            engineRepository.save(engineBd);
            return engineRepository.findByEngineNumber(engineBd.getEngineNumber());
        }
    }
}
