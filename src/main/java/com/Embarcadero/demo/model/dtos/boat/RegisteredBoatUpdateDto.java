package com.Embarcadero.demo.model.dtos.boat;

import com.Embarcadero.demo.model.dtos.engine.EngineUpdateDto;
import com.Embarcadero.demo.model.entities.enums.TypeLicencedBoat_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredBoatUpdateDto {
    private EngineUpdateDto engine;
    private String hull;
    private String name;
    private Integer capacity;
    private TypeLicencedBoat_enum typeLicencedBoat_enum;
}
