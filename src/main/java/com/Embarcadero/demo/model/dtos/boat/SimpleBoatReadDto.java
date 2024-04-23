package com.Embarcadero.demo.model.dtos.boat;

import com.Embarcadero.demo.model.entities.enums.TypeSimpleBoat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SimpleBoatReadDto {
    Integer id;
    TypeSimpleBoat typeSimpleBoat;
    String details;
    String notes;
}
