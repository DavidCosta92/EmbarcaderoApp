package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.boat.*;
import com.Embarcadero.demo.model.entities.Engine;
import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.boat.SimpleBoat;
import com.Embarcadero.demo.model.mappers.BoatMapper;
import com.Embarcadero.demo.model.repositories.RegisteredBoatRepository;
import com.Embarcadero.demo.model.repositories.SimpleBoatRepository;
import com.Embarcadero.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoatService {

    @Autowired
    SimpleBoatRepository simpleBoatRepository;
    @Autowired
    RegisteredBoatRepository registeredBoatRepository;
    @Autowired
    private BoatMapper boatMapper;
    @Autowired
    private EngineService engineService;

    @Autowired
    private Validator validator;


    public RegisteredBoat addBoat(RegisteredBoatAddDto registeredBoatAddDto){
        validateNewBoat(registeredBoatAddDto);
        engineService.addEngine(registeredBoatAddDto.getEngine());
        registeredBoatRepository.save(boatMapper.toEntity(registeredBoatAddDto));
        return getByName(registeredBoatAddDto.getName());
    }

    public SimpleBoat addBoat(SimpleBoatAddDto addDto){
        return simpleBoatRepository.save(boatMapper.toEntity(addDto));
    }


    public void validateNewBoat(RegisteredBoatAddDto registeredBoatAddDto){
        validateNewName(registeredBoatAddDto.getName());
        engineService.validateNewEngine(registeredBoatAddDto.getEngine());
    }
    public RegisteredBoat getByName(String name){
        Optional<RegisteredBoat> boat = registeredBoatRepository.findByName(name);
        if(boat.isEmpty()) throw new NotFoundException("No se encontro bote con el nombre:"+name+".");
        return boat.get();
    }
    public RegisteredBoatReadDto findByName(String name){
        return boatMapper.toReadDto(getByName(name));
    }
    public void validateNewName(String name){
        validator.stringText("Nombre", name);
        validator.stringMinSize("Nombre", 2 , name);
    }

    public RegisteredBoat updateBoat(RegisteredBoat boatBd, RegisteredBoatUpdateDto newBoat){
        if(newBoat.getEngine() != null && newBoat.getHull() != null && newBoat.getName() != null && newBoat.getCapacity() != null && newBoat.getTypeLicencedBoat() != null ){
            return addBoat(boatMapper.toAddDto(newBoat)); // SI OWNER UPDATE TRAE TODOS LOS DATOS.. CREO UN OWNER NUEVO Y LO devuelvo
        } else{
            // Uso el boatDB y actualizo los datos que vienen, valido, guardo y devuelvo boatDB actualizado
            if (newBoat.getEngine() != null) {
                Engine updatedEngine = engineService.updateEngine(boatBd.getEngine() , newBoat.getEngine());
                boatBd.setEngine(updatedEngine);
            }
            if (newBoat.getHull() != null) {
                validator.stringText("Casco",newBoat.getHull());
                boatBd.setHull(newBoat.getHull());
            }
            if (newBoat.getName() != null) {
                validateNewName(newBoat.getName());
                boatBd.setName(newBoat.getName());
            }
            if (newBoat.getCapacity() != null) {
                validator.stringOnlyIntegerPositiveNumbers("Capacidad", String.valueOf(newBoat.getCapacity()));
                boatBd.setCapacity(newBoat.getCapacity());
            }
            if (newBoat.getTypeLicencedBoat() != null) {
                boatBd.setTypeLicencedBoat(newBoat.getTypeLicencedBoat());
            }
            registeredBoatRepository.save(boatBd);
        }
        return getByName(newBoat.getName());
    }
}
