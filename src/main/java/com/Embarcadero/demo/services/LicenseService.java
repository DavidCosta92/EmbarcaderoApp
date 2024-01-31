package com.Embarcadero.demo.services;

import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseArrayDto;
import com.Embarcadero.demo.model.entities.Boat;
import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.model.entities.Owner;
import com.Embarcadero.demo.model.mappers.LicenseMapper;
import com.Embarcadero.demo.model.repositories.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {
    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    private LicenseMapper licenseMapper;

    @Autowired
    private BoatService boatService;
    @Autowired
    private OwnerService ownerService;

    public LicenseArrayDto findAll (String licenseCode, Integer pageNumber, Integer pageSize, String sortBy){

        Page<License> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (licenseCode != null) {
            // todo validMatricula(licenseCode);
            results = licenseRepository.findAllByLicenseCodeContains(licenseCode, pageable);
        } else {
            results = licenseRepository.findAll(pageable);
        }
        Page pagedResults = results.map(entity -> licenseMapper.entityToReadDTO(entity));

        return LicenseArrayDto.builder()
                .licenses(pagedResults.getContent())
                .total_results(pagedResults.getTotalElements())
                .results_per_page(pageSize)
                .current_page(pageNumber)
                .pages(pagedResults.getTotalPages())
                .sort_by(sortBy)
                .build();
    }
    public LicenseReadDto addLicense(LicenseAddDto licenseAddDto){
        validateNewLicense(licenseAddDto);

        Boat boat = boatService.addBoat(licenseAddDto.getBoat());
        Owner owner = ownerService.getOrAddOwner(licenseAddDto.getOwner());

        License license = License.builder()
                .licenseCode(licenseAddDto.getLicenseCode())
                .boat(boat)
                .owner(owner)
                .state_enum(licenseAddDto.getState_enum())
                .build();
        licenseRepository.save(license);
        return licenseMapper.entityToReadDTO(license);
    }

    public void validateNewLicense(LicenseAddDto licenseAddDto){
        /*
        // TODO VALIDAR matriculaAddDto completa !!
            "matricula": null,
            "embarcacion": { .. ya validado .. },
            "duenio": { .. ya validado .. },
            "estado": "BAJA"
        }*/

        boatService.validateNewBoat(licenseAddDto.getBoat());
        ownerService.validateOwnerNewMatricula(licenseAddDto.getOwner());
    }
}
