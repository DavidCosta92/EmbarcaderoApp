package com.Embarcadero.demo.model.dtos.records;

import com.Embarcadero.demo.model.dtos.boat.BoatReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordAddDto {
    private Integer idShift;
    // private Date startTime;
    // private Date endTime;
    // private RecordState_enum recordState;

    //Integer numberOfGuests; todo mediante clases @Valid
    //String car; todo mediante clases @Valid
    //String notes; todo mediante clases @Valid

    private BoatReadDto boat;
    private PersonReadDto person;
    private Integer numberOfGuests;
    private String car;
    private String notes;

    private Boolean hasLicense;
}
