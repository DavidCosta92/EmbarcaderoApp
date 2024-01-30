package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.motor.MotorAddDto;
import com.Embarcadero.demo.model.dtos.motor.MotorReadDto;
import com.Embarcadero.demo.model.entities.Motor;
import com.Embarcadero.demo.model.mappers.MotorMapper;
import com.Embarcadero.demo.model.repositories.MotorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotorService {
    @Autowired
    private MotorRepository motorRepository;

    @Autowired
    private MotorMapper motorMapper;

    public MotorReadDto addMotor(MotorAddDto motorAddDto){
        validateNewMotor(motorAddDto);
        motorRepository.save(motorMapper.motorAddToEntity(motorAddDto));
        return findMotorByNumero_motor(motorAddDto.getNumero_motor());
    }
    public Motor getMotorByNumero_motor (String numeroMotor){
        return motorRepository.findByNumeroMotor(numeroMotor);
    }
    public void validateNewMotor(MotorAddDto motorAddDto){
        if (motorRepository.existsByNumeroMotor(motorAddDto.getNumero_motor())) throw new AlreadyExistException("Numero de motor ya existe");
    }

    public MotorReadDto findMotorByNumero_motor(String numeroMotor){
        return motorMapper.entityToReadDto(getMotorByNumero_motor(numeroMotor));
    }
}
