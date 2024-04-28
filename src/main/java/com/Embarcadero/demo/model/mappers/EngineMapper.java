package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.engine.EngineAddDto;
import com.Embarcadero.demo.model.dtos.engine.EngineReadDto;
import com.Embarcadero.demo.model.dtos.engine.EngineUpdateDto;
import com.Embarcadero.demo.model.entities.Engine;
import org.springframework.stereotype.Component;

@Component
public class EngineMapper {

    public Engine toEntity(EngineAddDto engineAddDto){
        return new Engine().builder()
                .engineType(engineAddDto.getEngineType())
                .engineNumber(engineAddDto.getEngineNumber())
                .cc(engineAddDto.getCc())
                .notes(engineAddDto.getNotes())
                .build();
    }
    public Engine toEntity(EngineReadDto engine){
        return new Engine().builder()
                .id(engine.getId())
                .engineType(engine.getEngineType())
                .engineNumber(engine.getEngineNumber())
                .cc(engine.getCc())
                .notes(engine.getNotes())
                .build();
    }
    public EngineReadDto toReadDto (Engine engine){
        return new EngineReadDto().builder()
                .id(engine.getId())
                .engineType(engine.getEngineType())
                .engineNumber(engine.getEngineNumber())
                .cc(engine.getCc())
                .notes(engine.getNotes())
                .build();
    }
    public EngineAddDto toAddDto (EngineUpdateDto engine){
        return new EngineAddDto().builder()
                .engineType(engine.getEngineType())
                .engineNumber(engine.getEngineNumber())
                .cc(engine.getCc())
                .notes(engine.getNotes())
                .build();
    }
    public EngineAddDto toAddDto (Engine engine){
        return new EngineAddDto().builder()
                .engineType(engine.getEngineType())
                .engineNumber(engine.getEngineNumber())
                .cc(engine.getCc())
                .notes(engine.getNotes())
                .build();
    }
    public EngineUpdateDto toUpdateDto (Engine engine){
        return new EngineUpdateDto().builder()
                .engineType(engine.getEngineType())
                .engineNumber(engine.getEngineNumber())
                .cc(engine.getCc())
                .notes(engine.getNotes())
                .build();
    }


}
