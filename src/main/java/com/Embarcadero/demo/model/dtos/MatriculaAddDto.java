package com.Embarcadero.demo.model.dtos;

import com.Embarcadero.demo.model.dtos.embarcacion.EmbarcacionAddDto;
import com.Embarcadero.demo.model.dtos.persona.PersonaAddDto;
import com.Embarcadero.demo.model.entities.enums.Estado_enum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatriculaAddDto {
    @NotNull(message = "Matricula no puede ser nula")
    @Size(min=5, max=30, message = "Matricula debe tener entre 5 y 30 caracteres")
    private String matricula;

    private EmbarcacionAddDto embarcacion;
    private PersonaAddDto duenio;
    private Estado_enum estadoEnum;
}
