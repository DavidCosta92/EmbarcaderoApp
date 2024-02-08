package com.Embarcadero.demo.model.entities.enums;

public enum RecordState_enum {
    ACTIVO, // MIENTRAS ESTA EN AGUA
    EGRESADO, // UNA VEZ QUE SALE
    DESCONOCIDO, // SI NO SE REGISTRA EGRESO Y YA PASO UN TIEMPO PRUDENCIAL (24HS?)
    SINIESTRADO, // OCURRIO UN EVENTO ESPECIAL
    ELIMINADO    // ERROR DE CARGA ADMINISTRATIVO
}
