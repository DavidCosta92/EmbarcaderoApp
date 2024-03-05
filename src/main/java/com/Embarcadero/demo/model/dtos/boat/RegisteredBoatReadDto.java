package com.Embarcadero.demo.model.dtos.boat;

import com.Embarcadero.demo.model.dtos.engine.EngineReadDto;
import com.Embarcadero.demo.model.entities.enums.TypeLicencedBoat_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredBoatReadDto {
    private Integer id;
    private EngineReadDto engine;
    private String hull;
    private String name;
    private Integer capacity;
    private TypeLicencedBoat_enum typeLicencedBoat_enum;

    private String details;
    public RegisteredBoatReadDto (String name){
        // este constructor es para aquellos casos donde solo envio el nombre, en el caso de agregar un bote a un record
        this.name = name;
    }
}