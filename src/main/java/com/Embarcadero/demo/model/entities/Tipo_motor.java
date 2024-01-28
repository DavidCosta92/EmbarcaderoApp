package com.Embarcadero.demo.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tipo_motor {
    MOTOR_INTERNO,
    FUERA_DE_BORDA ,
    SIN_MOTOR,
    OTRO
}
