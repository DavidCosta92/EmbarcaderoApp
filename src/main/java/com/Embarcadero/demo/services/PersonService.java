package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.dtos.records.RecordUpdateDto;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.Record;
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

    public PersonReadDto createPerson(PersonAddDto addDto){
        if(personDtoIsComplete(addDto)){
            validateNewPerson(addDto);
            Person createdPerson = personRepository.save(personMapper.toEntity(addDto));
            return personMapper.toReadDto(createdPerson);
        }
        return null;
    }
    public PersonReadDto updatePerson(PersonUpdateDto updateDto){
        Person personBd= getPersonByDni(updateDto.getDni());
        return personMapper.toReadDto(updatePersonFields(personBd, updateDto));
    }


    public Person getOrAddPersonForLicensesOrRecord(PersonAddDto personAddDto){
        validatePersonNewMatriculaOrNewRecord(personAddDto);
        if(personDtoIsComplete(personAddDto)){
           personRepository.save(personMapper.toEntity(personAddDto));
        }
        return getPersonByDni(personAddDto.getDni());
    }

    public Person updateRecordPerson(Record recordBd, RecordUpdateDto updateDto){
        PersonAddDto addDto = personMapper.toAddDto(updateDto.getPerson());

        if(!updateDto.getPerson().getIsUpdate()){  //CREO UN Person NUEVO Y LO devuelvo
            return getOrAddPersonForLicensesOrRecord(addDto);
        } else { //Si es un update, asumo que la persona ya deberia existir y hay que actualizarla
            if(personAddDtoOnlyHasDni(addDto)){
                // si solo tiene dni, asumo que es un cambio, solo la obtengo y la seteo
                Person bdPerson = getPersonByDni(updateDto.getPerson().getDni());
                return updatePersonFields(bdPerson, updateDto.getPerson()); // actualiza los campos que tengan info y devuelve person
            } else {
                // persona trae dni o no y algun otro campo, obtengo la persona desde el record, seteo los campos y la retorno
                return updatePersonFields(recordBd.getPerson(), updateDto.getPerson()); // actualiza los campos que tengan info y devuelve person
            }
        }
    }
    public Person updatePersonFields(Person bdPerson, PersonUpdateDto newPerson){
        // Uso el Person y actualizo los datos que vienen, valido, guardo y devuelvo Person actualizado
        if (newPerson.getDni() != null) {
            // todo bdPerson.setDni(newPerson.getDni()); por regla de negocio, no deberia permitirse editar el dni de una persona, pero si es necesario que venga para saber a quien hay que cambiarle los datos? ..
        }
        if (newPerson.getPhone() != null) {
            validator.validPhoneNumber(newPerson.getPhone());
            bdPerson.setPhone(newPerson.getPhone());
        }
        if (newPerson.getName() != null) {
            validator.stringMinSize("Nombre", 3, newPerson.getName());
            validator.stringOnlyLetters("Nombre", newPerson.getName());
            bdPerson.setName(newPerson.getName());
        }
        if (newPerson.getLastName() != null) {
            validator.stringMinSize("Apellido", 3, newPerson.getLastName());
            validator.stringOnlyLetters("Apellido", newPerson.getLastName());
            bdPerson.setLastName(newPerson.getLastName());
        }
        if (newPerson.getEmergency_phone() != null) {
            validator.validPhoneNumber(newPerson.getEmergency_phone());
            bdPerson.setEmergency_phone(newPerson.getEmergency_phone());
        }
        if (newPerson.getAddress() != null) {
            validator.stringMinSize("Direccion", 5 , newPerson.getAddress());
            validator.stringText("Direccion", newPerson.getAddress());
            bdPerson.setAddress(newPerson.getAddress());
        }
        if (newPerson.getNotes() != null) {
            bdPerson.setNotes(newPerson.getNotes());
        }
        personRepository.save(bdPerson);
        return getPersonByDni(bdPerson.getDni());
    }
    public Person updatePerson(Person bdPerson, PersonUpdateDto newPerson){
        if(personDtoIsComplete(newPerson)){
            return getOrAddPersonForLicensesOrRecord(personMapper.toAddDto(newPerson)); // SI Person UPDATE TRAE TODOS LOS DATOS.. CREO UN Person NUEVO Y LO devuelvo
        } else{
            return updatePersonFields(bdPerson, newPerson); // Si no trae todos, los mando a actualziar a los que traiga Y LO devuelvo
        }
    }
    public Person getPersonByDni(String dni){
        Person bdPerson = personRepository.findByDni(dni);
        if(bdPerson == null) throw new NotFoundException("No se encontro persona por DNI:"+dni);
        return bdPerson;
    }
    public PersonReadDto findPersonByDni(String dni){
        return personMapper.toReadDto(getPersonByDni(dni));
    }
    public void validatePersonNewMatriculaOrNewRecord(PersonAddDto personAddDto){
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
        validator.stringText("Direccion", personAddDto.getAddress());
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
