package com.Embarcadero.demo.model.dtos.boat;


import com.Embarcadero.demo.model.entities.enums.TypeSimpleBoat_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleBoatUpdateDto {
    TypeSimpleBoat_enum typeSimpleBoat_enum;
    String details;
    String notes;
}
