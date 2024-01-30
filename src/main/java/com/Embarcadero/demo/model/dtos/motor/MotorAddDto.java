package com.Embarcadero.demo.model.dtos.motor;

import com.Embarcadero.demo.model.entities.enums.TipoMotor_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MotorAddDto {
    private TipoMotor_enum tipo_motorenum;
    private String numero_motor;
    private String cilindrada;
    private String observaciones;
}
