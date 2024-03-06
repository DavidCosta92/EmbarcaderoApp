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
                .owner(license.getOwner())
                .registeredBoat(license.getRegisteredBoat())
                .licenseState_enum(license.getLicenseState_enum())
                .notes(license.getNotes())
                .build();
    }

    public License toEntity (LicenseReadDto license){
        return new License().builder()
                .id(license.getId())
                .licenseCode(license.getLicenseCode())
                .owner(license.getOwner())
                .registeredBoat(license.getRegisteredBoat())
                .licenseState_enum(license.getLicenseState_enum())
                .notes(license.getNotes())
                .build();

    }

}
