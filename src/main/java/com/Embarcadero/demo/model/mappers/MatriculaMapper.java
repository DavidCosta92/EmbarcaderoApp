package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.MatriculaReadDto;
import com.Embarcadero.demo.model.entities.Matricula;
import org.springframework.stereotype.Component;

@Component
public class MatriculaMapper {

    public MatriculaReadDto entityToReadDTO (Matricula matricula){
        return new MatriculaReadDto().builder()
                .id(matricula.getId())
                .duenio(matricula.getDuenio())
                .embarcacion(matricula.getEmbarcacion())
                .estado(matricula.getEstado())
                .build();
    }

}
