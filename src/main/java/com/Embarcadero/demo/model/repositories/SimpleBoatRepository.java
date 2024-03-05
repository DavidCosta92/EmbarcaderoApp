package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.boat.SimpleBoat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimpleBoatRepository extends JpaRepository<SimpleBoat, Integer> {

}
