package com.Embarcadero.demo.model.dtos.license;

import com.Embarcadero.demo.model.dtos.boat.BoatUpdateDto;
import com.Embarcadero.demo.model.dtos.owner.OwnerUpdateDto;
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
    private BoatUpdateDto boat;
    private OwnerUpdateDto owner;
    private State_enum state_enum;
}
