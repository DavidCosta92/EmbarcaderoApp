package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.boat.BoatAddDto;
import com.Embarcadero.demo.model.dtos.boat.BoatUpdateDto;
import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseArrayDto;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.model.dtos.owner.OwnerAddDto;
import com.Embarcadero.demo.model.dtos.owner.OwnerUpdateDto;
import com.Embarcadero.demo.model.entities.Boat;
import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.model.entities.Owner;
import com.Embarcadero.demo.model.entities.enums.State_enum;
import com.Embarcadero.demo.model.mappers.LicenseMapper;
import com.Embarcadero.demo.model.repositories.LicenseRepository;
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
    public License getById(Integer id){
        // TODO validateInteger??
        Optional<License> license = licenseRepository.findById(id);
        if (license.isEmpty())  throw new NotFoundException("No se encontro Licencia");
        return license.get();
    }
    public LicenseReadDto findById(Integer id){
        return licenseMapper.entityToReadDTO(getById(id));
    }
    public LicenseReadDto updateById(Integer id , LicenseUpdateDto licenseUpdateDto){
        License licenseBD = getById(id); // obtener los datos originales en la base de datos
        // verificar que datos me envian.. y los que lleguen mandar a actualizarlos
        String licenseCode = licenseUpdateDto.getLicenseCode();
        BoatUpdateDto boat = licenseUpdateDto.getBoat();
        OwnerUpdateDto owner = licenseUpdateDto.getOwner();
        State_enum state = licenseUpdateDto.getState_enum();
        if (licenseCode != null){
            // TODO VALIDAR DATOS A ACTUALIZAR
            licenseBD.setLicenseCode(licenseCode);
            licenseRepository.save(licenseBD);
        }
        if (state!= null){
            // TODO VALIDAR DATOS A ACTUALIZAR
            licenseBD.setState_enum(state);
            licenseRepository.save(licenseBD);
        }
        if (boat!= null){
            // TODO VALIDAR DATOS A ACTUALIZAR
            // TODO licenseBD.setBoat(boat);
            // TODO licenseRepository.save(licenseBD);

        }
        if (owner != null){
            // TODO VALIDAR DATOS A ACTUALIZAR
            licenseBD.setOwner(owner);
            licenseRepository.save(licenseBD);

        }


        // guardar el objeto actualizado en la bd

        // TODO y retornar el objeto actualizado
        return new LicenseReadDto();
    }
    public LicenseReadDto deleteById(Integer id){
        LicenseReadDto licenseReadDto = findById(id);
        licenseRepository.deleteById(id);
        return licenseReadDto;
    }


    public void existsById(Integer id){
        if(!licenseRepository.existsById(id)) throw new NotFoundException("No existe licencia por id");
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
