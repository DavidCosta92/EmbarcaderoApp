package com.Embarcadero.demo.model.dtos;

import com.Embarcadero.demo.model.entities.Embarcacion;
import com.Embarcadero.demo.model.entities.Estado;
import com.Embarcadero.demo.model.entities.Persona;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatriculaReadDto {
    private Integer id;
    private String matricula;
    private Embarcacion embarcacion;
    private Persona duenio;
    private Estado estado;
}
