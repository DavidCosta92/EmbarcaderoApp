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
import com.Embarcadero.demo.model.entities.enums.LicenseState;
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
                .code(licenseAddDto.getCode())
                .registeredBoat(boat)
                .owner(person)
                .state(licenseAddDto.getState())
                .notes(licenseAddDto.getNotes())
                .build();
        licenseRepository.save(license);
        return licenseMapper.toReadDTO(license);
    }
    public void validateCode(String code){
        if(licenseRepository.existsByCode(code)) throw new AlreadyExistException("Matricula ya existente!");
    }
    public void validateNewLicense(LicenseAddDto licenseAddDto){
        validateCode(licenseAddDto.getCode());
        boatService.validateNewBoat(licenseAddDto.getRegisteredBoat());
        personService.validatePersonNewMatriculaOrNewRecord(licenseAddDto.getOwner());
        if (licenseAddDto.getState() == null) licenseAddDto.setState(LicenseState.OK);
    }
    public LicenseReadDtoArray findAll (String code, String searchValue, Integer pageNumber, Integer pageSize, String sortBy){
        Page<License> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (code != null) {
            results = licenseRepository.findAllByCodeContains(code, pageable);
        } else if(code == null && searchValue !=null && !searchValue.equals("")){
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
    public LicenseReadDto findByCode(String code){
        return licenseMapper.toReadDTO(getByCode(code));
    }

    public LicenseReadDto updateById(Integer id , LicenseUpdateDto licenseUpdateDto){
        License licenseBD = getById(id); // obtener los datos originales en la base de datos
        // verificar que datos me envian.. y los que lleguen mandar a actualizarlos
        String code = licenseUpdateDto.getCode();
        RegisteredBoatUpdateDto boat = licenseUpdateDto.getBoat();
        PersonUpdateDto personToUpdate = licenseUpdateDto.getOwner();
        LicenseState state = licenseUpdateDto.getState();
        if (code != null){
            validator.stringMinSize("Matricula",5 , code);
            validator.stringText("Matricula" , code);
            licenseBD.setCode(code);
        }
        if (state!= null){
            licenseBD.setState(state);
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

    public License getByCode(String code){
        Optional<License> license = licenseRepository.findByCode(code);
        if (license.isEmpty())  throw new NotFoundException("No se encontro Licencia con codigo: "+code);
        return license.get();
    }

}
