package com.Embarcadero.demo.model.dtos.boat;

import com.Embarcadero.demo.model.entities.enums.TypeSimpleBoat_enum;
import jakarta.persistence.*;
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
    TypeSimpleBoat_enum typeSimpleBoat_enum;
    String details;
    String notes;
}
