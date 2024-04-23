package com.Embarcadero.demo.model.dtos.boat;


import com.Embarcadero.demo.model.entities.enums.TypeSimpleBoat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleBoatUpdateDto {
    TypeSimpleBoat typeSimpleBoat;
    String details;
    String notes;
}
