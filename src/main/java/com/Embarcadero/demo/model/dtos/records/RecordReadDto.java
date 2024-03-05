package com.Embarcadero.demo.model.dtos.records;

import com.Embarcadero.demo.model.dtos.boat.SimpleBoatReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.entities.License;
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
public class RecordReadDto {
    private Integer id;
    private Date startTime;
    private Date endTime;
    private RecordState_enum recordState;
    private License license;
    private SimpleBoatReadDto simpleBoat;
    private PersonReadDto person;
    private Integer numberOfGuests;
    private String car;
    private String notes;
}
