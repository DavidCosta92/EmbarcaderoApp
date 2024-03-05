package com.Embarcadero.demo.model.dtos.boat;

import com.Embarcadero.demo.model.entities.enums.TypeSimpleBoat_enum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SimpleBoatAddDto {
    TypeSimpleBoat_enum typeSimpleBoat_enum;

    @NotNull(message = "Detalles no pueden ser nulos")
    @Size(min=3, max=255, message = "nombre debe tener entre 3 y 25 caracteres")
    String details;
    String notes;
}
