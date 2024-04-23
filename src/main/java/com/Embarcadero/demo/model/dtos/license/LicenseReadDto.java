package com.Embarcadero.demo.model.dtos.license;

import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.enums.LicenseState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseReadDto {
    private Integer id;
    private String code;
    private RegisteredBoat registeredBoat;
    private Person owner;
    private LicenseState state;
    private String notes;
}
