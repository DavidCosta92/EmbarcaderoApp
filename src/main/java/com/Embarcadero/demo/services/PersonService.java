package com.Embarcadero.demo.services;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.mappers.PersonMapper;
import com.Embarcadero.demo.model.repositories.PersonRepository;
import com.Embarcadero.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private Validator validator;

    public Person getOrAddPerson(PersonAddDto personAddDto){
        validatePersonNewMatricula(personAddDto);
        if(personDtoIsComplete(personAddDto)){
           personRepository.save(personMapper.toEntity(personAddDto));
        }
        return personRepository.findByDni(personAddDto.getDni());
    }
    public Person updatePerson(Person bdPerson, PersonUpdateDto newPerson){
        if(personDtoIsComplete(newPerson)){
            return getOrAddPerson(personMapper.toAddDto(newPerson)); // SI Person UPDATE TRAE TODOS LOS DATOS.. CREO UN Person NUEVO Y LO devuelvo
        } else{
            // Uso el Person y actualizo los datos que vienen, valido, guardo y devuelvo Person actualizado
            if (newPerson.getDni() != null) {
                bdPerson.setDni(newPerson.getDni());
            }
            if (newPerson.getPhone() != null) {
                validator.validPhoneNumber(newPerson.getPhone());
                bdPerson.setPhone(newPerson.getPhone());
            }
            if (newPerson.getName() != null) {
                validator.stringMinSize("Nombre", 3, newPerson.getName());
                validator.stringOnlyLetters("Nombre", newPerson.getName());
                bdPerson.setDni(newPerson.getDni());
            }
            if (newPerson.getLastName() != null) {
                validator.stringMinSize("Apellido", 3, newPerson.getLastName());
                validator.stringOnlyLetters("Apellido", newPerson.getLastName());
                validator.validPhoneNumber(newPerson.getPhone());
                bdPerson.setPhone(newPerson.getPhone());
            }
            if (newPerson.getEmergency_phone() != null) {
                validator.validPhoneNumber(newPerson.getEmergency_phone());
                bdPerson.setEmergency_phone(newPerson.getEmergency_phone());
            }
            if (newPerson.getAddress() != null) {
                validator.stringMinSize("Direccion", 5 , newPerson.getAddress());
                validator.stringOnlyLettersAndNumbers("Direccion", newPerson.getAddress());
                bdPerson.setAddress(newPerson.getAddress());
            }
            if (newPerson.getNotes() != null) {
                bdPerson.setNotes(newPerson.getNotes());
            }
            personRepository.save(bdPerson);
            return personRepository.findByDni(bdPerson.getDni());
        }
    }
    public void validatePersonNewMatricula(PersonAddDto personAddDto){
        if(personAddDtoOnlyHasDni(personAddDto)){ // si solo viene dni, asumo que ya deberia existir la persona, por lo que deberia traer de bd
            validateAlreadyReportedPerson(personAddDto);
        } else {
            validateNewPerson(personAddDto);
        }
    }
    public void validateNewPerson(PersonAddDto personAddDto){
        validator.stringMinSize("Nombre", 3, personAddDto.getName());
        validator.stringOnlyLetters("Nombre", personAddDto.getName());

        validator.stringMinSize("Apellido", 3, personAddDto.getLastName());
        validator.stringOnlyLetters("Apellido", personAddDto.getLastName());

        validator.validPhoneNumber(personAddDto.getPhone());
        validator.validPhoneNumber(personAddDto.getEmergency_phone());
        validator.stringOnlyLettersAndNumbers("Direccion", personAddDto.getAddress());
        if(personRepository.existsByDni(personAddDto.getDni())) throw new AlreadyExistException("Persona ya existe con ese dni");
    }
    public void validateAlreadyReportedPerson(PersonAddDto personAddDto){
        if(!personRepository.existsByDni(personAddDto.getDni())) throw new AlreadyExistException("Persona no existe, revisa dni o crea una nueva persona");
    }


    public Boolean personAddDtoOnlyHasDni (PersonAddDto personAddDto){
        if(personAddDto.getPhone() == null && personAddDto.getEmergency_phone() == null && personAddDto.getName() == null && personAddDto.getLastName() == null && personAddDto.getAddress() == null && personAddDto.getNotes() == null && personAddDto.getDni() != null ){
            return true;
        }
        return false;
    }
    public Boolean personDtoIsComplete(PersonAddDto personAddDto ){
        if(personAddDto.getName() != null && personAddDto.getLastName() != null && personAddDto.getPhone() != null && personAddDto.getEmergency_phone() != null && personAddDto.getAddress() != null && personAddDto.getNotes() != null && personAddDto.getDni() != null ){
            return true;
        }
        return false;
    }
    public Boolean personDtoIsComplete(PersonUpdateDto personUpdateDto ){
        if(personUpdateDto.getName() != null && personUpdateDto.getLastName() != null && personUpdateDto.getPhone() != null && personUpdateDto.getEmergency_phone() != null && personUpdateDto.getAddress() != null && personUpdateDto.getNotes() != null && personUpdateDto.getDni() != null ){
            return true;
        }
        return false;
    }
}
