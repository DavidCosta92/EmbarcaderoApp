package com.Embarcadero.demo.model.dtos.license;

import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.enums.State_enum;
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
    private String licenseCode;
    private RegisteredBoat registeredBoat;
    private Person person;
    private State_enum state_enum;
}
