package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Engine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineRepository extends JpaRepository<Engine, Integer> {

    Engine findByEngineNumber(String engineNumber);

    Boolean existsByEngineNumber(String engineNumber);
}
