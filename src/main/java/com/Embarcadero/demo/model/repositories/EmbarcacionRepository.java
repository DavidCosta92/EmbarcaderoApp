package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Embarcacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbarcacionRepository extends JpaRepository<Embarcacion , Integer> {
    Embarcacion findByNombre (String nombre);
    Boolean existsByNombre (String nombre);

}
