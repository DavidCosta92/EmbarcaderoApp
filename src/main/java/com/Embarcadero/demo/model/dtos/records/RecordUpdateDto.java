package com.Embarcadero.demo.model.dtos.records;

import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.entities.boat.SimpleBoat;
import com.Embarcadero.demo.model.entities.enums.RecordState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordUpdateDto {
    private Integer idShift;

    // @Valid todo VALIDAR LICENSE EN SERVICE PORQUE ES OPCIONAL!
    // private License license; // Es un read porque solo envio license code
    private LicenseUpdateDto license;
    // @Valid todo VALIDAR PERSONA EN SERVICE PORQUE ES OPCIONAL!
    private PersonUpdateDto person;
    // @Valid todo VALIDAR BOAT EN SERVICE PORQUE ES OPCIONAL!
    private SimpleBoat simpleBoat;

    // todo VALIDAR DATOS EN SHIFT SERVICE PORQUE ES OPCIONAL!
    private RecordState recordState;
    private Integer numberOfGuests;
    private String car;
    private String notes;
    private Boolean hasLicense;
}