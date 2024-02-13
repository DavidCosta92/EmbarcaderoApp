package com.Embarcadero.demo.model.dtos.records;

import com.Embarcadero.demo.model.dtos.boat.BoatAddDto;
import com.Embarcadero.demo.model.dtos.boat.BoatReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordAddDto {
    private Integer idShift;

    private BoatReadDto boat; // Es un read porque solo envio nombre de embarcacion..

    private Date startTime;
    private RecordState_enum recordState;

    @Valid
    private PersonAddDto person;

    @NotNull(message = "La cantidad de acompañantes no puede ser nula, el valor correspondiente deberia ser 0")
    @PositiveOrZero(message = "La cantidad de acompañantes debe ser igual o mayor a 0")
    private Integer numberOfGuests;

    @NotNull
    @Size(min = 6 , max = 10, message = "Patente debe tener entre 6 y 10 caracteres")
    private String car;
    private String notes;

    private Boolean hasLicense;
}
