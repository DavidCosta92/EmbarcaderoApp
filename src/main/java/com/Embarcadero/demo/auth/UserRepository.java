package com.Embarcadero.demo.auth;

import com.Embarcadero.demo.auth.entities.Role;
import com.Embarcadero.demo.auth.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Integer> {
    Optional<User> findByUsername (String username);
    Optional<User> findByEmail (String email);
    Optional<User> findByDni (String dni);
    Boolean existsByUsername (String username);
    Boolean existsByEmail (String email);
    Boolean existsByDni (String dni);

    Page<User> findAllByRoleAndDniContains (Role role, String dni, Pageable pageable);

    Page<User> findAllByRole (Role role, Pageable pageable);


}
