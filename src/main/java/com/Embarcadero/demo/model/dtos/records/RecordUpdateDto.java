package com.Embarcadero.demo.model.dtos.records;

import com.Embarcadero.demo.model.dtos.boat.BoatReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
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

    // @Valid todo VALIDAR BOAT EN SERVICE PORQUE ES OPCIONAL!
    private BoatReadDto boat; // Es un read porque solo envio nombre de embarcacion..
    // @Valid todo VALIDAR PERSONA EN SERVICE PORQUE ES OPCIONAL!
    private PersonUpdateDto person;

    // todo VALIDAR DATOS EN SHIFT SERVICE PORQUE ES OPCIONAL!
    private RecordState_enum recordState;
    private Integer numberOfGuests;
    private String car;
    private String notes;
    private Boolean hasLicense;
}


