package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.boat.BoatAddDto;
import com.Embarcadero.demo.model.dtos.boat.BoatReadDto;
import com.Embarcadero.demo.model.entities.Boat;
import com.Embarcadero.demo.model.entities.Engine;
import com.Embarcadero.demo.services.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoatMapper {
    @Autowired
    private EngineMapper engineMapper;

    @Autowired
    private EngineService engineService;

    public Boat addToEntity(BoatAddDto boatAddDto){
        Engine engine = engineService.getEngineByEngineNumber(boatAddDto.getEngine().getEngineNumber());

        return new Boat().builder()
                .typeBoat_enum(boatAddDto.getTypeBoat_enum())
                .hull(boatAddDto.getHull())
                .engine(engine)
                .capacity(boatAddDto.getCapacity())
                .name(boatAddDto.getName())
                .build();
    }
    public BoatReadDto entityToReadDto(Boat boat){
        return new BoatReadDto().builder()
                .id(boat.getId())
                .engine(engineMapper.entityToReadDto(boat.getEngine()))
                .hull(boat.getHull())
                .name(boat.getName())
                .capacity(boat.getCapacity())
                .typeBoat_enum(boat.getTypeBoat_enum())
                .build();
    }
}
