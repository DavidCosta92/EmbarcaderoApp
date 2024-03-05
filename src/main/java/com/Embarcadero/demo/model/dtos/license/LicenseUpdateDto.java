package com.Embarcadero.demo.model.dtos.license;

import com.Embarcadero.demo.model.dtos.boat.RegisteredBoatUpdateDto;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.entities.enums.State_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseUpdateDto {
    private String licenseCode;
    private RegisteredBoatUpdateDto boat;
    private PersonUpdateDto person;
    private State_enum state_enum;
}
