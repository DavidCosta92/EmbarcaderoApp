package com.Embarcadero.demo.model.dtos.shift;

import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftAddDto {
    private Dam_enum dam;
    private Date date;

    // TODO TABLA RECORDS TIENE ID DE SHIFT

    // TODO Lista usuarios que estan de guardia, deben ser guardavidas..  relacion many to many

    private List<Person> staff;
}
