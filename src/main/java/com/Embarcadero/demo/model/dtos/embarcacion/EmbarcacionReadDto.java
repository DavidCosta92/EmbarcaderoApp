package com.Embarcadero.demo.model.dtos.embarcacion;

import com.Embarcadero.demo.model.dtos.motor.MotorReadDto;
import com.Embarcadero.demo.model.entities.enums.TipoEmbarcacion_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbarcacionReadDto {
    private Integer id;
    private MotorReadDto motor;
    private String casco;
    private String nombre;
    private Integer capacidad;
    private TipoEmbarcacion_enum tipo_embarcacionEnum;
}
