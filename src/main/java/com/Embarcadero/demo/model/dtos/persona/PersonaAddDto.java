package com.Embarcadero.demo.model.dtos.persona;

import com.Embarcadero.demo.model.entities.Matricula;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaAddDto {
    private String dni;
    private String telefono;
    private String telefono_emergencia;
    private String direccion;
    private String observaciones;
}
