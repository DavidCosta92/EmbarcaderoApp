package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.boat.BoatAddDto;
import com.Embarcadero.demo.model.dtos.boat.BoatUpdateDto;
import com.Embarcadero.demo.model.entities.Boat;
import com.Embarcadero.demo.model.entities.Engine;
import com.Embarcadero.demo.model.mappers.BoatMapper;
import com.Embarcadero.demo.model.repositories.BoatRepository;
import com.Embarcadero.demo.utils.Validator;
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

    @Autowired
    private Validator validator;


    public Boat addBoat(BoatAddDto boatAddDto){
        validateNewBoat(boatAddDto);
        engineService.addEngine(boatAddDto.getEngine());
        boatRepository.save(boatMapper.toEntity(boatAddDto));
        return findByName(boatAddDto.getName());
    }

    public void validateNewBoat(BoatAddDto boatAddDto){
        validateNewName(boatAddDto.getName());
        engineService.validateNewEngine(boatAddDto.getEngine());
    }
    public Boat findByName(String name){
        return boatRepository.findByName(name);
    }
    public void validateNewName(String name){
        validator.stringOnlyLettersAndNumbers("Nombre", name);
        validator.stringMinSize("Nombre", 2 , name);
        if (boatRepository.existsByName(name)) throw new AlreadyExistException("Nombre de embarcacion ya existe!");
    }

    public Boat updateBoat( Boat boatDB, BoatUpdateDto newBoat){
        if(newBoat.getEngine() != null && newBoat.getHull() != null && newBoat.getName() != null && newBoat.getCapacity() != null && newBoat.getTypeBoat_enum() != null ){
            return addBoat(boatMapper.toAddDto(newBoat)); // SI OWNER UPDATE TRAE TODOS LOS DATOS.. CREO UN OWNER NUEVO Y LO devuelvo
        } else{
            // Uso el boatDB y actualizo los datos que vienen, valido, guardo y devuelvo boatDB actualizado
            if (newBoat.getEngine() != null) {
                Engine updatedEngine = engineService.updateEngine(boatDB.getEngine() , newBoat.getEngine());
                boatDB.setEngine(updatedEngine);
            }
            if (newBoat.getHull() != null) {
                validator.stringOnlyLettersAndNumbers("Casco",newBoat.getHull());
                boatDB.setHull(newBoat.getHull());
            }
            if (newBoat.getName() != null) {
                validateNewName(newBoat.getName());
                boatDB.setName(newBoat.getName());
            }
            if (newBoat.getCapacity() != null) {
                validator.stringOnlyIntegerPositiveNumbers("Capacidad", String.valueOf(newBoat.getCapacity()));
                boatDB.setCapacity(newBoat.getCapacity());
            }
            if (newBoat.getTypeBoat_enum() != null) {
                boatDB.setTypeBoat_enum(newBoat.getTypeBoat_enum());
            }
            boatRepository.save(boatDB);
        }
        return boatRepository.findByName(newBoat.getName());
    }
}
