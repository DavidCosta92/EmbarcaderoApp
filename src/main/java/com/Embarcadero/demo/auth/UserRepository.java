package com.Embarcadero.demo.auth;

import com.Embarcadero.demo.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Integer> {
    Optional<User> findByUsername (String username);
    Optional<User> findByEmail (String email);
    Boolean existsByUsername (String username);
    Boolean existsByEmail (String email);
    Boolean existsByDni (String dni);

}
