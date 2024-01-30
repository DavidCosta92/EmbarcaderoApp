package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.persona.PersonaAddDto;
import com.Embarcadero.demo.model.entities.Persona;
import com.Embarcadero.demo.model.mappers.PersonaMapper;
import com.Embarcadero.demo.model.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {
    @Autowired
    private PersonaMapper personaMapper;

    @Autowired
    private PersonaRepository personaRepository;

    public Persona getOrAddPersona(PersonaAddDto personaAddDto){
        validateNewPersona(personaAddDto);
        if(personaAddDto.getTelefono() != null && personaAddDto.getTelefono_emergencia() != null && personaAddDto.getDireccion() != null && personaAddDto.getObservaciones() != null && personaAddDto.getDni() != null ){
           personaRepository.save(personaMapper.addDtoToEntity(personaAddDto));
        }
        return personaRepository.findByDni(personaAddDto.getDni());
    }
    public void validatePersonaNewMatricula(PersonaAddDto personaAddDto){
        if(personaAddDto.getTelefono() == null && personaAddDto.getTelefono_emergencia() == null && personaAddDto.getDireccion() == null && personaAddDto.getObservaciones() == null && personaAddDto.getDni() != null ){ // si solo viene dni, asumo que ya deberia existir la persona, por lo que deberia traer de bd
            validateAlreadyReportedPersona(personaAddDto);
        } else {
            validateNewPersona(personaAddDto);
        }
    }
    public void validateNewPersona(PersonaAddDto personaAddDto){
        // TODO VALIDAR DATOS DE INGRESO
        // TODO si persona viene con todos los datos, asumo que quiere crear una persona nueva, por tanto valido que no este duplicado el dni y que los otros esten bien
        if(personaRepository.existsByDni(personaAddDto.getDni())) throw new AlreadyExistException("Persona ya existe con ese dni");
    }
    public void validateAlreadyReportedPersona(PersonaAddDto personaAddDto){
        if(!personaRepository.existsByDni(personaAddDto.getDni())) throw new AlreadyExistException("Persona no existe, revisa dni o crea una nueva persona");
    }

}
