package com.Embarcadero.demo.model.dtos.license;

import com.Embarcadero.demo.model.dtos.boat.RegisteredBoatUpdateDto;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.entities.enums.LicenseState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseUpdateDto {
    private String code;
    private RegisteredBoatUpdateDto boat;
    private PersonUpdateDto owner;
    private LicenseState state;
    private String notes;
}
