package com.Embarcadero.demo.services;

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
        if(personAddDto.getPhone() != null && personAddDto.getEmergency_phone() != null && personAddDto.getAddress() != null && personAddDto.getNotes() != null && personAddDto.getDni() != null ){
           personRepository.save(personMapper.toEntity(personAddDto));
        }
        return personRepository.findByDni(personAddDto.getDni());
    }
    public Person updatePerson(Person bdPerson, PersonUpdateDto newPerson){
        if(newPerson.getDni() != null && newPerson.getPhone() != null && newPerson.getEmergency_phone() != null && newPerson.getAddress() != null && newPerson.getNotes() != null ){
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
        if(personAddDto.getPhone() == null && personAddDto.getEmergency_phone() == null && personAddDto.getAddress() == null && personAddDto.getNotes() == null && personAddDto.getDni() != null ){ // si solo viene dni, asumo que ya deberia existir la persona, por lo que deberia traer de bd
            validateAlreadyReportedPerson(personAddDto);
        } else {
            validateNewPerson(personAddDto);
        }
    }
    public void validateNewPerson(PersonAddDto personAddDto){
        validator.validPhoneNumber(personAddDto.getPhone());
        validator.validPhoneNumber(personAddDto.getEmergency_phone());
        validator.stringOnlyLettersAndNumbers("Direccion", personAddDto.getAddress());
        if(personRepository.existsByDni(personAddDto.getDni())) throw new AlreadyExistException("Persona ya existe con ese dni");
    }
    public void validateAlreadyReportedPerson(PersonAddDto personAddDto){
        if(!personRepository.existsByDni(personAddDto.getDni())) throw new AlreadyExistException("Persona no existe, revisa dni o crea una nueva persona");
    }

}
