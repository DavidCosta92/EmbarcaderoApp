package com.Embarcadero.demo.model.dtos.boat;

import com.Embarcadero.demo.model.dtos.engine.EngineAddDto;
import com.Embarcadero.demo.model.entities.enums.TypeLicencedBoat;
import jakarta.validation.constraints.Min;
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
public class RegisteredBoatAddDto {
    // @Valid => TODO ESTA VALIDACION ES OPCIONAL, SOLO SI LA EMBARCACION POSEE MOTOR.. POR TANTO DEBO HACECRLA CON validador.java
    private EngineAddDto engine;

    @NotNull(message = "Casco no puede ser nula")
    @Size(min=3, max=30, message = "Casco debe tener entre 3 y 30 caracteres")
    private String hull;

    @NotNull(message = "nombre no puede ser nula")
    @Size(min=2, max=30, message = "nombre debe tener entre 2 y 30 caracteres")
    private String name;

    @NotNull(message = "Capacidad no puede ser nula")
    @Min(value = 1 ,message = "Capacidad debe ser un valor positivo mayor a 0")
    private Integer capacity;

    private String details;

    private TypeLicencedBoat typeLicencedBoat;

}

