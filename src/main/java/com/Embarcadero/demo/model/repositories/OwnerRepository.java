package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    Boolean existsByDni(String dni);

    Owner findByDni (String dni);
}
