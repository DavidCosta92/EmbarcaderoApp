package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Integer> {
    Boat findByName(String name);
    Boolean existsByName(String name);

}
