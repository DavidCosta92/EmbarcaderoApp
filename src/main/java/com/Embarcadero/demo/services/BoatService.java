package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.boat.BoatAddDto;
import com.Embarcadero.demo.model.entities.Boat;
import com.Embarcadero.demo.model.mappers.BoatMapper;
import com.Embarcadero.demo.model.repositories.BoatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoatService {
    @Autowired
    private BoatRepository boatRepository;
    @Autowired
    private BoatMapper boatMapper;
    @Autowired
    private EngineService engineService;


    public Boat addBoat(BoatAddDto boatAddDto){
        validateNewBoat(boatAddDto);
        engineService.addEngine(boatAddDto.getEngine());
        boatRepository.save(boatMapper.addToEntity(boatAddDto));
        return findByName(boatAddDto.getName());
    }

    public void validateNewBoat(BoatAddDto boatAddDto){
        engineService.validateNewEngine(boatAddDto.getEngine());
        if(boatRepository.existsByName(boatAddDto.getName())) throw new AlreadyExistException("Nombre de embarcacion ya existe!");
    }
    public Boat findByName(String nombre){
        return boatRepository.findByName(nombre);
    }
}
