package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.persona.PersonaAddDto;
import com.Embarcadero.demo.model.dtos.persona.PersonaReadDto;
import com.Embarcadero.demo.model.entities.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public Persona addDtoToEntity (PersonaAddDto personaAddDto){
        return Persona.builder()
                .dni(personaAddDto.getDni())
                .telefono(personaAddDto.getTelefono())
                .telefono_emergencia(personaAddDto.getTelefono_emergencia())
                .direccion(personaAddDto.getDireccion())
                .observaciones(personaAddDto.getObservaciones())
                .build();
    }
    public PersonaReadDto entityToReadDto (Persona persona){
        return PersonaReadDto.builder()
                .id(persona.getId())
                .dni(persona.getDni())
                .telefono(persona.getTelefono())
                .telefono_emergencia(persona.getTelefono_emergencia())
                .direccion(persona.getDireccion())
                .observaciones(persona.getObservaciones())
                .build();
    }
    public PersonaAddDto entityToAddDto (Persona persona){
        return PersonaAddDto.builder()
                .dni(persona.getDni())
                .telefono(persona.getTelefono())
                .telefono_emergencia(persona.getTelefono_emergencia())
                .direccion(persona.getDireccion())
                .observaciones(persona.getObservaciones())
                .build();
    }

}
