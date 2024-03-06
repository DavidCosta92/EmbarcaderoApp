package com.Embarcadero.demo.model.dtos.license;

import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.enums.LicenseState_enum;
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
    private Person owner;
    private LicenseState_enum licenseState_enum;
    private String notes;
}
