package com.Embarcadero.demo.model.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EngineType_enum {
    MOTOR_INTERNO,
    FUERA_DE_BORDA ,
    SIN_MOTOR,
    OTRO
}
