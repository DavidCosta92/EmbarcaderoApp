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
                .phone(personAddDto.getPhone())
                .emergency_phone(personAddDto.getEmergency_phone())
                .address(personAddDto.getAddress())
                .notes(personAddDto.getNotes())
                .build();
    }
    public Person toEntity (PersonUpdateDto updateDto){
        return Person.builder()
                .dni(updateDto.getDni())
                .phone(updateDto.getPhone())
                .emergency_phone(updateDto.getEmergency_phone())
                .address(updateDto.getAddress())
                .notes(updateDto.getNotes())
                .build();
    }



    public PersonReadDto toReadDto(Person person){
        return PersonReadDto.builder()
                .id(person.getId())
                .dni(person.getDni())
                .phone(person.getPhone())
                .emergency_phone(person.getEmergency_phone())
                .address(person.getAddress())
                .notes(person.getNotes())
                .build();
    }




    public PersonAddDto toAddDto(PersonUpdateDto updateDto){
        return PersonAddDto.builder()
                .dni(updateDto.getDni())
                .phone(updateDto.getPhone())
                .emergency_phone(updateDto.getEmergency_phone())
                .address(updateDto.getAddress())
                .notes(updateDto.getNotes())
                .build();
    }
    public PersonAddDto toAddDto (Person person){
        return PersonAddDto.builder()
                .dni(person.getDni())
                .phone(person.getPhone())
                .emergency_phone(person.getEmergency_phone())
                .address(person.getAddress())
                .notes(person.getNotes())
                .build();
    }
}
