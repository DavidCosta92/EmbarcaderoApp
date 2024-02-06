package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.boat.BoatAddDto;
import com.Embarcadero.demo.model.dtos.boat.BoatUpdateDto;
import com.Embarcadero.demo.model.entities.Boat;
import com.Embarcadero.demo.model.entities.Engine;
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
        boatRepository.save(boatMapper.toEntity(boatAddDto));
        return findByName(boatAddDto.getName());
    }

    public void validateNewBoat(BoatAddDto boatAddDto){
        engineService.validateNewEngine(boatAddDto.getEngine());
        if(boatRepository.existsByName(boatAddDto.getName())) throw new AlreadyExistException("Nombre de embarcacion ya existe!");
    }
    public Boat findByName(String nombre){
        return boatRepository.findByName(nombre);
    }

    public Boat updateBoat( Boat boatDB, BoatUpdateDto newBoat){
        if(newBoat.getEngine() != null && newBoat.getHull() != null && newBoat.getName() != null && newBoat.getCapacity() != null && newBoat.getTypeBoat_enum() != null ){
            return addBoat(boatMapper.toAddDto(newBoat)); // SI OWNER UPDATE TRAE TODOS LOS DATOS.. CREO UN OWNER NUEVO Y LO devuelvo
        } else{
            // Uso el boatDB y actualizo los datos que vienen, valido, guardo y devuelvo boatDB actualizado
            if (newBoat.getEngine() != null) {
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales

                Engine updatedEngine = engineService.updateEngine(boatDB.getEngine() , newBoat.getEngine());

                boatDB.setEngine(updatedEngine);
            }
            if (newBoat.getHull() != null) {
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
                boatDB.setHull(newBoat.getHull());
            }
            if (newBoat.getName() != null) {
                // TODO si viene, debo validar que no exista previamente el nombre
                // TODO si viene, debo validar que no exista previamente el nombre
                // TODO si viene, debo validar que no exista previamente el nombre
                // TODO si viene, debo validar que no exista previamente el nombre
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
                boatDB.setName(newBoat.getName());
            }
            if (newBoat.getCapacity() != null) {
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
                boatDB.setCapacity(newBoat.getCapacity());
            }
            if (newBoat.getTypeBoat_enum() != null) {
                // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
                boatDB.setTypeBoat_enum(newBoat.getTypeBoat_enum());
            }
            boatRepository.save(boatDB);
        }
        return boatRepository.findByName(newBoat.getName());
    }
}
