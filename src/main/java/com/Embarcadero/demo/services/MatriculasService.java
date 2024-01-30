package com.Embarcadero.demo.services;

import com.Embarcadero.demo.model.dtos.MatriculaAddDto;
import com.Embarcadero.demo.model.dtos.MatriculaReadDto;
import com.Embarcadero.demo.model.dtos.MatriculasArrayDto;
import com.Embarcadero.demo.model.entities.Embarcacion;
import com.Embarcadero.demo.model.entities.Matricula;
import com.Embarcadero.demo.model.entities.Persona;
import com.Embarcadero.demo.model.mappers.MatriculaMapper;
import com.Embarcadero.demo.model.repositories.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MatriculasService {
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private MatriculaMapper matriculaMapper;

    @Autowired
    private EmbarcacionService embarcacionService;
    @Autowired
    private PersonaService personaService;

    public MatriculasArrayDto findAll (String matricula, Integer pageNumber, Integer pageSize, String sortBy){

        Page<Matricula> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (matricula != null) {
            // todo validMatricula(matricula);
            results = matriculaRepository.findAllByMatriculaContains(matricula, pageable);
        } else {
            results = matriculaRepository.findAll(pageable);
        }
        Page pagedResults = results.map(entity -> matriculaMapper.entityToReadDTO(entity));

        return MatriculasArrayDto.builder()
                .matriculas(pagedResults.getContent())
                .total_results(pagedResults.getTotalElements())
                .results_per_page(pageSize)
                .current_page(pageNumber)
                .pages(pagedResults.getTotalPages())
                .sort_by(sortBy)
                .build();
    }
    public MatriculaReadDto addMatricula (MatriculaAddDto matriculaAddDto){
        validateNewMatricula(matriculaAddDto);

        Embarcacion embarcacion = embarcacionService.addEmbarcacion(matriculaAddDto.getEmbarcacion());
        Persona persona = personaService.getOrAddPersona(matriculaAddDto.getDuenio());

        Matricula matricula = Matricula.builder()
                .matricula(matriculaAddDto.getMatricula())
                .embarcacion(embarcacion)
                .duenio(persona)
                .estadoEnum(matriculaAddDto.getEstadoEnum())
                .build();
        // matriculaRepository.save(matriculaMapper.addDtoToEntity(matriculaAddDto));
        matriculaRepository.save(matricula);
        return matriculaMapper.entityToReadDTO(matricula);
    }

    public void validateNewMatricula(MatriculaAddDto matriculaAddDto){
        /*
        // TODO VALIDAR matriculaAddDto completa !!
            "matricula": null,
            "embarcacion": { .. ya validado .. },
            "duenio": { .. ya validado .. },
            "estado": "BAJA"
        }*/

        embarcacionService.validateNewEmbarcacion(matriculaAddDto.getEmbarcacion());
        personaService.validatePersonaNewMatricula(matriculaAddDto.getDuenio());
    }
}
