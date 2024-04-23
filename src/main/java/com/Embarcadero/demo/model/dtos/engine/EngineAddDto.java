package com.Embarcadero.demo.model.dtos.engine;

import com.Embarcadero.demo.model.entities.enums.EngineType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineAddDto {
    private EngineType engineType;

    @NotNull(message = "Numero de motor no puede ser nulo.")
    @Size(min = 7, max = 30, message = "Numero de motor debe contener de 7 a 30 digitos")
    private String engineNumber;

    @NotNull(message = "Cilindradas no puede ser nula.")
    @Size(min = 2, max = 4, message = "Cilindradas debe contener de 2 a 4 digitos")
    private String cc;

    private String notes;
}
