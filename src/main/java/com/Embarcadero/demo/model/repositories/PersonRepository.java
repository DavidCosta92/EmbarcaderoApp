package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.auth.entities.Role;
import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.model.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Boolean existsByDni(String dni);

    Person findByDni (String dni);

    Page<Person> findAllByDniContains(String dni, Pageable pageable);

    @Query("SELECT p FROM Person p WHERE ( p.lastName LIKE %:searchValue% OR p.name LIKE %:searchValue%  OR p.dni LIKE %:searchValue%)")
    Page<Person> getAllBySearchValueContains (String searchValue, Pageable pageable);
}
