package com.Embarcadero.demo.model.dtos.persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaReadDto {
    private Integer id;
    private String dni;
    private String telefono;
    private String telefono_emergencia;
    private String direccion;
    private String observaciones;
}
