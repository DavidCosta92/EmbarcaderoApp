package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Motor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotorRepository  extends JpaRepository<Motor, Integer> {

    Motor findByNumeroMotor(String numero_motor);

    Boolean existsByNumeroMotor(String numero_motor);
}
