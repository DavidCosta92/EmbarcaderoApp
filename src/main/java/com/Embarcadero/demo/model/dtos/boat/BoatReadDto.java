package com.Embarcadero.demo.model.dtos.boat;

import com.Embarcadero.demo.model.dtos.engine.EngineReadDto;
import com.Embarcadero.demo.model.entities.enums.TypeBoat_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoatReadDto {
    private Integer id;
    private EngineReadDto engine;
    private String hull;
    private String name;
    private Integer capacity;
    private TypeBoat_enum typeBoat_enum;
}
