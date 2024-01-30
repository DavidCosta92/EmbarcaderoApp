package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.embarcacion.EmbarcacionAddDto;
import com.Embarcadero.demo.model.dtos.embarcacion.EmbarcacionReadDto;
import com.Embarcadero.demo.model.entities.Embarcacion;
import com.Embarcadero.demo.model.entities.Motor;
import com.Embarcadero.demo.services.MotorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmbarcacionMapper {
    @Autowired
    private MotorMapper motorMapper;

    @Autowired
    private MotorService motorService;

    public Embarcacion addToEntity(EmbarcacionAddDto embarcacionAddDto){
        Motor motor = motorService.getMotorByNumero_motor(embarcacionAddDto.getMotor().getNumero_motor());

        return new Embarcacion().builder()
                .tipo_embarcacionEnum(embarcacionAddDto.getTipo_embarcacionEnum())
                .casco(embarcacionAddDto.getCasco())
                .motor(motor)
                .capacidad(embarcacionAddDto.getCapacidad())
                .nombre(embarcacionAddDto.getNombre())
                .build();
    }
    public EmbarcacionReadDto entityToReadDto(Embarcacion embarcacion){
        return new EmbarcacionReadDto().builder()
                .id(embarcacion.getId())
                .motor(motorMapper.entityToReadDto(embarcacion.getMotor()))
                .casco(embarcacion.getCasco())
                .nombre(embarcacion.getNombre())
                .capacidad(embarcacion.getCapacidad())
                .tipo_embarcacionEnum(embarcacion.getTipo_embarcacionEnum())
                .build();
    }
}
