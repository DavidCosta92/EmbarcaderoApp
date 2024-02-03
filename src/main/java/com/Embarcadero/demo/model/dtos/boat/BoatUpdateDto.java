package com.Embarcadero.demo.model.dtos.boat;

import com.Embarcadero.demo.model.dtos.engine.EngineUpdateDto;
import com.Embarcadero.demo.model.entities.enums.TypeBoat_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoatUpdateDto {
    private EngineUpdateDto engine;
    private String hull;
    private String name;
    private Integer capacity;
    private TypeBoat_enum typeBoat_enum;
}
