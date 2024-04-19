package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.boat.RegisteredBoatUpdateDto;
import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDtoArray;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.enums.LicenseState_enum;
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
    private PersonService personService;

    @Autowired
    private Validator validator;

    public LicenseReadDto addLicense(LicenseAddDto licenseAddDto){
        validateNewLicense(licenseAddDto);
        RegisteredBoat boat = boatService.addBoat(licenseAddDto.getRegisteredBoat());
        Person person = personService.getOrAddPersonForLicensesOrRecord(licenseAddDto.getOwner());

        License license = License.builder()
                .licenseCode(licenseAddDto.getLicenseCode())
                .registeredBoat(boat)
                .owner(person)
                .licenseState_enum(licenseAddDto.getLicenseState_enum())
                .notes(licenseAddDto.getNotes())
                .build();
        licenseRepository.save(license);
        return licenseMapper.toReadDTO(license);
    }
    public void validateLicenseCode(String licenseCode){
        if(licenseRepository.existsByLicenseCode(licenseCode)) throw new AlreadyExistException("Matricula ya existente!");
    }
    public void validateNewLicense(LicenseAddDto licenseAddDto){
        validateLicenseCode(licenseAddDto.getLicenseCode());
        boatService.validateNewBoat(licenseAddDto.getRegisteredBoat());
        personService.validatePersonNewMatriculaOrNewRecord(licenseAddDto.getOwner());
        if (licenseAddDto.getLicenseState_enum() == null) licenseAddDto.setLicenseState_enum(LicenseState_enum.OK);
    }
    public LicenseReadDtoArray findAll (String licenseCode, String searchValue, Integer pageNumber, Integer pageSize, String sortBy){
        Page<License> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (licenseCode != null) {
            results = licenseRepository.findAllByLicenseCodeContains(licenseCode, pageable);
        } else if(licenseCode == null && !searchValue.equals("")){
            results = licenseRepository.getAllBySearchValueContains(searchValue, pageable);
        }else {
            results = licenseRepository.findAll(pageable);
        }
        Page pagedResults = results.map(entity -> licenseMapper.toReadDTO(entity));

        return LicenseReadDtoArray.builder()
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
    public LicenseReadDto findByLicenseCode(String licenseCode){
        return licenseMapper.toReadDTO(getByLicenseCode(licenseCode));
    }

    public LicenseReadDto updateById(Integer id , LicenseUpdateDto licenseUpdateDto){
        License licenseBD = getById(id); // obtener los datos originales en la base de datos
        // verificar que datos me envian.. y los que lleguen mandar a actualizarlos
        String licenseCode = licenseUpdateDto.getLicenseCode();
        RegisteredBoatUpdateDto boat = licenseUpdateDto.getBoat();
        PersonUpdateDto personToUpdate = licenseUpdateDto.getOwner();
        LicenseState_enum state = licenseUpdateDto.getLicenseState_enum();
        if (licenseCode != null){
            validator.stringMinSize("Matricula",5 , licenseCode);
            validator.stringText("Matricula" , licenseCode);
            licenseBD.setLicenseCode(licenseCode);
        }
        if (state!= null){
            licenseBD.setLicenseState_enum(state);
        }
        if (boat!= null){
            RegisteredBoat updatedBoatELIMINAR =  boatService.updateBoat(licenseBD.getRegisteredBoat() , boat);
            licenseBD.setRegisteredBoat(updatedBoatELIMINAR);
        }
        if (personToUpdate != null){
            Person updatedPerson =  personService.updatePerson(licenseBD.getOwner() , personToUpdate);
            licenseBD.setOwner(updatedPerson);
        }
        licenseRepository.save(licenseBD);
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

    public License getByLicenseCode(String licenseCode){
        Optional<License> license = licenseRepository.findByLicenseCode(licenseCode);
        if (license.isEmpty())  throw new NotFoundException("No se encontro Licencia con codigo: "+licenseCode);
        return license.get();
    }

}
