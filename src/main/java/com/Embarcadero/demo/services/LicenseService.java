package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.boat.BoatUpdateDto;
import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDtoArray;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.entities.Boat;
import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.model.entities.Person;
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
    private PersonService personService;

    @Autowired
    private Validator validator;


    public LicenseReadDto addLicense(LicenseAddDto licenseAddDto){
        validateNewLicense(licenseAddDto);
        Boat boat = boatService.addBoat(licenseAddDto.getBoat());
        Person person = personService.getOrAddPerson(licenseAddDto.getPerson());

        License license = License.builder()
                .licenseCode(licenseAddDto.getLicenseCode())
                .boat(boat)
                .person(person)
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
        personService.validatePersonNewMatricula(licenseAddDto.getPerson());
        if (licenseAddDto.getLicenseCode() == null) licenseAddDto.setLicenseCode(State_enum.OK.name());
    }
    public LicenseReadDtoArray findAll (String licenseCode, Integer pageNumber, Integer pageSize, String sortBy){
        Page<License> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (licenseCode != null) {
            results = licenseRepository.findAllByLicenseCodeContains(licenseCode, pageable);
        } else {
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
    public LicenseReadDto updateById(Integer id , LicenseUpdateDto licenseUpdateDto){
        License licenseBD = getById(id); // obtener los datos originales en la base de datos
        // verificar que datos me envian.. y los que lleguen mandar a actualizarlos
        String licenseCode = licenseUpdateDto.getLicenseCode();
        BoatUpdateDto boat = licenseUpdateDto.getBoat();
        PersonUpdateDto personToUpdate = licenseUpdateDto.getPerson();
        State_enum state = licenseUpdateDto.getState_enum();
        if (licenseCode != null){
            validator.stringMinSize("Matricula",5 , licenseCode);
            validator.stringOnlyLettersAndNumbers("Matricula" , licenseCode);
            licenseBD.setLicenseCode(licenseCode);
        }
        if (state!= null){
            licenseBD.setState_enum(state);
        }
        if (boat!= null){
            Boat updatedBoat =  boatService.updateBoat(licenseBD.getBoat() , boat);
            licenseBD.setBoat(updatedBoat);
        }
        if (personToUpdate != null){
            Person updatedPerson =  personService.updatePerson(licenseBD.getPerson() , personToUpdate);
            licenseBD.setPerson(updatedPerson);
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

}
