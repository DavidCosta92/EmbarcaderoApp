package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.entities.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public Person toEntity (PersonAddDto personAddDto){
        return Person.builder()
                .dni(personAddDto.getDni())
                .name(personAddDto.getName())
                .lastName(personAddDto.getLastName())
                .phone(personAddDto.getPhone())
                .emergencyPhone(personAddDto.getEmergencyPhone())
                .address(personAddDto.getAddress())
                .notes(personAddDto.getNotes())
                .build();
    }
    public Person toEntity (PersonReadDto readDto){
        return Person.builder()
                .dni(readDto.getDni())
                .name(readDto.getName())
                .lastName(readDto.getLastName())
                .phone(readDto.getPhone())
                .emergencyPhone(readDto.getEmergencyPhone())
                .address(readDto.getAddress())
                .notes(readDto.getNotes())
                .build();
    }
    public Person toEntity (PersonUpdateDto updateDto){
        return Person.builder()
                .dni(updateDto.getDni())
                .name(updateDto.getName())
                .lastName(updateDto.getLastName())
                .phone(updateDto.getPhone())
                .emergencyPhone(updateDto.getEmergencyPhone())
                .address(updateDto.getAddress())
                .notes(updateDto.getNotes())
                .build();
    }



    public PersonReadDto toReadDto(Person person){
        return PersonReadDto.builder()
                .id(person.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .dni(person.getDni())
                .phone(person.getPhone())
                .emergencyPhone(person.getEmergencyPhone())
                .address(person.getAddress())
                .notes(person.getNotes())
                .build();
    }


    public PersonUpdateDto toUpdateDto(PersonAddDto addDto){
        return PersonUpdateDto.builder()
                .dni(addDto.getDni())
                .name(addDto.getName())
                .lastName(addDto.getLastName())
                .phone(addDto.getPhone())
                .emergencyPhone(addDto.getEmergencyPhone())
                .address(addDto.getAddress())
                .notes(addDto.getNotes())
                .build();
    }

    public PersonAddDto toAddDto(PersonUpdateDto updateDto){
        return PersonAddDto.builder()
                .dni(updateDto.getDni())
                .name(updateDto.getName())
                .lastName(updateDto.getLastName())
                .phone(updateDto.getPhone())
                .emergencyPhone(updateDto.getEmergencyPhone())
                .address(updateDto.getAddress())
                .notes(updateDto.getNotes())
                .build();
    }
    public PersonAddDto toAddDto (Person person){
        return PersonAddDto.builder()
                .dni(person.getDni())
                .name(person.getName())
                .lastName(person.getLastName())
                .phone(person.getPhone())
                .emergencyPhone(person.getEmergencyPhone())
                .address(person.getAddress())
                .notes(person.getNotes())
                .build();
    }
}
