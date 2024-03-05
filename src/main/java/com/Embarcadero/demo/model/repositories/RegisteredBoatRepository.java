package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredBoatRepository extends JpaRepository<RegisteredBoat, Integer> {
    Optional<RegisteredBoat> findByName(String name);
    Boolean existsByName(String name);
}
