package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.engine.EngineAddDto;
import com.Embarcadero.demo.model.dtos.engine.EngineReadDto;
import com.Embarcadero.demo.model.entities.Engine;
import org.springframework.stereotype.Component;

@Component
public class EngineMapper {

    public Engine addToEntity(EngineAddDto engineAddDto){
        return new Engine().builder()
                .engineType_enum(engineAddDto.getEngineType_enum())
                .engineNumber(engineAddDto.getEngineNumber())
                .cc(engineAddDto.getCc())
                .notes(engineAddDto.getNotes())
                .build();
    }
    public EngineReadDto entityToReadDto (Engine engine){
        return new EngineReadDto().builder()
                .id(engine.getId())
                .engineType_enum(engine.getEngineType_enum())
                .engineNumber(engine.getEngineNumber())
                .cc(engine.getCc())
                .notes(engine.getNotes())
                .build();
    }
}
