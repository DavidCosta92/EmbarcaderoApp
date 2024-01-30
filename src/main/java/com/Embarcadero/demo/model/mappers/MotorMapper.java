package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.motor.MotorAddDto;
import com.Embarcadero.demo.model.dtos.motor.MotorReadDto;
import com.Embarcadero.demo.model.entities.Motor;
import org.springframework.stereotype.Component;

@Component
public class MotorMapper {

    public Motor motorAddToEntity (MotorAddDto motorAddDto){
        return new Motor().builder()
                .tipo_motorenum(motorAddDto.getTipo_motorenum())
                .numeroMotor(motorAddDto.getNumero_motor())
                .cilindrada(motorAddDto.getCilindrada())
                .observaciones(motorAddDto.getObservaciones())
                .build();
    }
    public MotorReadDto entityToReadDto (Motor motor){
        return new MotorReadDto().builder()
                .id(motor.getId())
                .tipo_motorenum(motor.getTipo_motorenum())
                .numero_motor(motor.getNumeroMotor())
                .cilindrada(motor.getCilindrada())
                .observaciones(motor.getObservaciones())
                .build();
    }
}
