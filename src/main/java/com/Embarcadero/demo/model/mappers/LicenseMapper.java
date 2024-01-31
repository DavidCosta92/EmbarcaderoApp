package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.services.BoatService;
import com.Embarcadero.demo.services.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LicenseMapper {
    @Autowired
    BoatService boatService;
    @Autowired
    OwnerService ownerService;

    public LicenseReadDto entityToReadDTO (License license){
        return new LicenseReadDto().builder()
                .id(license.getId())
                .licenseCode(license.getLicenseCode())
                .owner(license.getOwner())
                .boat(license.getBoat())
                .state_enum(license.getState_enum())
                .build();
    }
}
