package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.boat.*;
import com.Embarcadero.demo.model.entities.Engine;
import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.boat.SimpleBoat;
import com.Embarcadero.demo.services.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoatMapper {
    @Autowired
    private EngineMapper engineMapper;

    @Autowired
    private EngineService engineService;

    public RegisteredBoat toEntity(RegisteredBoatAddDto registeredBoatAddDto){
        Engine engine = engineService.getEngineByEngineNumber(registeredBoatAddDto.getEngine().getEngineNumber());

        return new RegisteredBoat().builder()
                .typeLicencedBoat_enum(registeredBoatAddDto.getTypeLicencedBoat_enum())
                .hull(registeredBoatAddDto.getHull())
                .engine(engine)
                .capacity(registeredBoatAddDto.getCapacity())
                .name(registeredBoatAddDto.getName())
                .build();
    }
    public SimpleBoat toEntity(SimpleBoatAddDto addDto){
        return new SimpleBoat().builder()
                .typeSimpleBoat_enum(addDto.getTypeSimpleBoat_enum())
                .details(addDto.getDetails())
                .notes(addDto.getNotes())
                .build();
    }
    public SimpleBoat toEntity(SimpleBoatReadDto readDto){
        return new SimpleBoat().builder()
                .typeSimpleBoat_enum(readDto.getTypeSimpleBoat_enum())
                .details(readDto.getDetails())
                .notes(readDto.getNotes())
                .build();
    }
    public RegisteredBoat toEntity(RegisteredBoatReadDto readDto){
        Engine engine = engineService.getEngineByEngineNumber(readDto.getEngine().getEngineNumber());

        return new RegisteredBoat().builder()
                .id(readDto.getId())
                .typeLicencedBoat_enum(readDto.getTypeLicencedBoat_enum())
                .hull(readDto.getHull())
                .engine(engine)
                .capacity(readDto.getCapacity())
                .name(readDto.getName())
                .build();
    }
    public RegisteredBoatReadDto toReadDto(RegisteredBoat registeredBoat){
        return new RegisteredBoatReadDto().builder()
                .id(registeredBoat.getId())
                .engine(engineMapper.toReadDto(registeredBoat.getEngine()))
                .hull(registeredBoat.getHull())
                .name(registeredBoat.getName())
                .capacity(registeredBoat.getCapacity())
                .typeLicencedBoat_enum(registeredBoat.getTypeLicencedBoat_enum())
                .build();
    }
        public SimpleBoatReadDto toReadDto(SimpleBoat simpleBoat){
        return new SimpleBoatReadDto().builder()
                .id(simpleBoat.getId())
                .typeSimpleBoat_enum(simpleBoat.getTypeSimpleBoat_enum())
                .details(simpleBoat.getDetails())
                .notes(simpleBoat.getNotes())
                .build();
    }
    public RegisteredBoatAddDto toAddDto(RegisteredBoatUpdateDto boat){
        return new RegisteredBoatAddDto().builder()
                .engine(engineMapper.toAddDto(boat.getEngine()))
                .hull(boat.getHull())
                .name(boat.getName())
                .capacity(boat.getCapacity())
                .typeLicencedBoat_enum(boat.getTypeLicencedBoat_enum())
                .build();
    }
}


