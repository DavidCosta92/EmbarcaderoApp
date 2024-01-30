package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.MatriculaReadDto;
import com.Embarcadero.demo.model.entities.Matricula;
import com.Embarcadero.demo.services.EmbarcacionService;
import com.Embarcadero.demo.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatriculaMapper {
    @Autowired
    EmbarcacionService embarcacionService;
    @Autowired
    PersonaService personaService;

    public MatriculaReadDto entityToReadDTO (Matricula matricula){
        return new MatriculaReadDto().builder()
                .id(matricula.getId())
                .matricula(matricula.getMatricula())
                .duenio(matricula.getDuenio())
                .embarcacion(matricula.getEmbarcacion())
                .estadoEnum(matricula.getEstadoEnum())
                .build();
    }
    /*
    public Matricula addDtoToEntity (MatriculaAddDto matriculaAddDto){
        Embarcacion emb = embarcacionService.findByNombre(matriculaAddDto.getEmbarcacion().getNombre());
        Persona duenio = personaService.getOrAddPersona(matriculaAddDto.getDuenio());
        return Matricula.builder()
                .matricula(matriculaAddDto.getMatricula())
                .embarcacion(emb)
                .duenio(duenio)
                .estado(matriculaAddDto.getEstado())
                .build();
    }
     */
}
