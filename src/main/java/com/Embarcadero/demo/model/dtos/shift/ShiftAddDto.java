package com.Embarcadero.demo.model.dtos.shift;

import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftAddDto {
    @NotNull
    private Dam_enum dam;

    // date se genera automaticamente por regla de negocio

    // TODO TABLA RECORDS TIENE ID DE SHIFT

    // TODO Lista usuarios que estan de guardia, deben ser guardavidas..  relacion many to many

    private List<Person> staff;


    private String notes;
}
