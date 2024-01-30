package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.embarcacion.EmbarcacionAddDto;
import com.Embarcadero.demo.model.dtos.embarcacion.EmbarcacionReadDto;
import com.Embarcadero.demo.model.dtos.motor.MotorReadDto;
import com.Embarcadero.demo.model.entities.Embarcacion;
import com.Embarcadero.demo.model.mappers.EmbarcacionMapper;
import com.Embarcadero.demo.model.repositories.EmbarcacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmbarcacionService {
    @Autowired
    private EmbarcacionRepository embarcacionRepository;
    @Autowired
    private EmbarcacionMapper embarcacionMapper;
    @Autowired
    private MotorService motorService;


    public Embarcacion addEmbarcacion (EmbarcacionAddDto embarcacionAddDto){
        validateNewEmbarcacion(embarcacionAddDto);
        motorService.addMotor(embarcacionAddDto.getMotor());
        embarcacionRepository.save(embarcacionMapper.addToEntity(embarcacionAddDto));
        return findByNombre(embarcacionAddDto.getNombre());
    }

    public void validateNewEmbarcacion(EmbarcacionAddDto embarcacionAddDto){
        motorService.validateNewMotor(embarcacionAddDto.getMotor());
        if(embarcacionRepository.existsByNombre(embarcacionAddDto.getNombre())) throw new AlreadyExistException("Nombre de embarcacion ya existe!");
    }
    public Embarcacion findByNombre (String nombre){
        return embarcacionRepository.findByNombre(nombre);
    }
}
