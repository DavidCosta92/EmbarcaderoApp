package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.services.BoatService;
import com.Embarcadero.demo.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LicenseMapper {
    @Autowired
    BoatService boatService;
    @Autowired
    PersonService personService;

    public LicenseReadDto toReadDTO(License license){
        return new LicenseReadDto().builder()
                .id(license.getId())
                .licenseCode(license.getLicenseCode())
                .person(license.getOwner())
                .registeredBoat(license.getRegisteredBoat())
                .state_enum(license.getState_enum())
                .build();
    }

    public License toEntity (LicenseReadDto license){
        return new License().builder()
                .id(license.getId())
                .licenseCode(license.getLicenseCode())
                .owner(license.getPerson())
                .registeredBoat(license.getRegisteredBoat())
                .state_enum(license.getState_enum())
                .build();

    }

}
