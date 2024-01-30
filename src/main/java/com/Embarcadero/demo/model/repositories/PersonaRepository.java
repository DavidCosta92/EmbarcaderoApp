package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository  extends JpaRepository<Persona , Integer> {

    Boolean existsByDni(String dni);

    Persona findByDni (String dni);
}
