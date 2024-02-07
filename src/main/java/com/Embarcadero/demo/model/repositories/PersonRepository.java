package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Boolean existsByDni(String dni);

    Person findByDni (String dni);
}
