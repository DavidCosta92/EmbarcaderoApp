package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.boat.BoatAddDto;
import com.Embarcadero.demo.model.dtos.boat.BoatReadDto;
import com.Embarcadero.demo.model.dtos.boat.BoatUpdateDto;
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

    public Boat toEntity(BoatAddDto boatAddDto){
        Engine engine = engineService.getEngineByEngineNumber(boatAddDto.getEngine().getEngineNumber());

        return new Boat().builder()
                .typeBoat_enum(boatAddDto.getTypeBoat_enum())
                .hull(boatAddDto.getHull())
                .engine(engine)
                .capacity(boatAddDto.getCapacity())
                .name(boatAddDto.getName())
                .build();
    }
    public Boat toEntity(BoatReadDto readDto){
        Engine engine = engineService.getEngineByEngineNumber(readDto.getEngine().getEngineNumber());

        return new Boat().builder()
                .typeBoat_enum(readDto.getTypeBoat_enum())
                .hull(readDto.getHull())
                .engine(engine)
                .capacity(readDto.getCapacity())
                .name(readDto.getName())
                .build();
    }
    public BoatReadDto toReadDto(Boat boat){
        return new BoatReadDto().builder()
                .id(boat.getId())
                .engine(engineMapper.toReadDto(boat.getEngine()))
                .hull(boat.getHull())
                .name(boat.getName())
                .capacity(boat.getCapacity())
                .typeBoat_enum(boat.getTypeBoat_enum())
                .build();
    }
    public BoatAddDto toAddDto(BoatUpdateDto boat){
        return new BoatAddDto().builder()
                .engine(engineMapper.toAddDto(boat.getEngine()))
                .hull(boat.getHull())
                .name(boat.getName())
                .capacity(boat.getCapacity())
                .typeBoat_enum(boat.getTypeBoat_enum())
                .build();
    }
}


