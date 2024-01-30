package com.Embarcadero.demo.model.dtos.embarcacion;

import com.Embarcadero.demo.model.dtos.motor.MotorAddDto;
import com.Embarcadero.demo.model.entities.enums.TipoEmbarcacion_enum;
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

public class EmbarcacionAddDto {
    private MotorAddDto motor;

    @NotNull(message = "Casco no puede ser nula")
    @Size(min=3, max=30, message = "Casco debe tener entre 3 y 30 caracteres")
    private String casco;

    @NotNull(message = "nombre no puede ser nula")
    @Size(min=2, max=30, message = "nombre debe tener entre 2 y 30 caracteres")
    private String nombre;

    @NotNull(message = "Capacidad no puede ser nula")
    private Integer capacidad;

    private TipoEmbarcacion_enum tipo_embarcacionEnum;

}
