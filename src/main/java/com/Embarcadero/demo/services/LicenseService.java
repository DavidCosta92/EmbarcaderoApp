package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.boat.BoatUpdateDto;
import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseArrayDto;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.model.dtos.owner.OwnerUpdateDto;
import com.Embarcadero.demo.model.entities.Boat;
import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.model.entities.Owner;
import com.Embarcadero.demo.model.entities.enums.State_enum;
import com.Embarcadero.demo.model.mappers.LicenseMapper;
import com.Embarcadero.demo.model.repositories.LicenseRepository;
import com.Embarcadero.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Autowired
    private Validator validator;


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
        return licenseMapper.toReadDTO(license);
    }
    public void validateLicenseCode(String licenseCode){
        if(licenseRepository.existsByLicenseCode(licenseCode)) throw new AlreadyExistException("Matricula ya existente!");
    }
    public void validateNewLicense(LicenseAddDto licenseAddDto){
        validateLicenseCode(licenseAddDto.getLicenseCode());
        boatService.validateNewBoat(licenseAddDto.getBoat());
        ownerService.validateOwnerNewMatricula(licenseAddDto.getOwner());
        if (licenseAddDto.getLicenseCode() == null) licenseAddDto.setLicenseCode(State_enum.OK.name());
    }
    public LicenseArrayDto findAll (String licenseCode, Integer pageNumber, Integer pageSize, String sortBy){
        Page<License> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (licenseCode != null) {
            results = licenseRepository.findAllByLicenseCodeContains(licenseCode, pageable);
        } else {
            results = licenseRepository.findAll(pageable);
        }
        Page pagedResults = results.map(entity -> licenseMapper.toReadDTO(entity));

        return LicenseArrayDto.builder()
                .licenses(pagedResults.getContent())
                .total_results(pagedResults.getTotalElements())
                .results_per_page(pageSize)
                .current_page(pageNumber)
                .pages(pagedResults.getTotalPages())
                .sort_by(sortBy)
                .build();
    }
    public License getById(Integer id){
        Optional<License> license = licenseRepository.findById(id);
        if (license.isEmpty())  throw new NotFoundException("No se encontro Licencia");
        return license.get();
    }
    public LicenseReadDto findById(Integer id){
        return licenseMapper.toReadDTO(getById(id));
    }
    public LicenseReadDto updateById(Integer id , LicenseUpdateDto licenseUpdateDto){
        License licenseBD = getById(id); // obtener los datos originales en la base de datos
        // verificar que datos me envian.. y los que lleguen mandar a actualizarlos
        String licenseCode = licenseUpdateDto.getLicenseCode();
        BoatUpdateDto boat = licenseUpdateDto.getBoat();
        OwnerUpdateDto ownerToUpdate = licenseUpdateDto.getOwner();
        State_enum state = licenseUpdateDto.getState_enum();
        if (licenseCode != null){
            // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
            licenseBD.setLicenseCode(licenseCode);
        }
        if (state!= null){
            // TODO VALIDAR DATOS DE INGRESO CON "utils/validator.java" => porque son opcionales
            licenseBD.setState_enum(state);
        }
        if (boat!= null){
            Boat updatedBoat =  boatService.updateBoat(licenseBD.getBoat() , boat);  //ownerService.updateOwner(licenseBD.getOwner() , ownerToUpdate);
            licenseBD.setBoat(updatedBoat);
        }
        if (ownerToUpdate != null){
            Owner updatedOwner =  ownerService.updateOwner(licenseBD.getOwner() , ownerToUpdate);
            licenseBD.setOwner(updatedOwner);
        }

        licenseRepository.save(licenseBD);
        //return licenseMapper.toReadDTO(getById(id));
        return licenseMapper.toReadDTO(licenseBD);
    }
    public LicenseReadDto deleteById(Integer id){
        LicenseReadDto licenseReadDto = findById(id);
        licenseRepository.deleteById(id);
        return licenseReadDto;
    }


    public void existsById(Integer id){
        if(!licenseRepository.existsById(id)) throw new NotFoundException("No existe licencia por id");
    }

}