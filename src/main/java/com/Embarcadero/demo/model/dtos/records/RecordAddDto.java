package com.Embarcadero.demo.model.dtos.records;

import com.Embarcadero.demo.model.dtos.boat.SimpleBoatAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
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

    // TODO SOLO ACCEPTABLE QUE VENGA UNO, no pueden venir o faltar ambos..
    private LicenseReadDto license; // Es un read porque solo envio license code
    private SimpleBoatAddDto simpleBoat; // es un add porque simpre creare un bote nuevo, ya que es un bote por cada registro!
    private Boolean hasLicense;
}