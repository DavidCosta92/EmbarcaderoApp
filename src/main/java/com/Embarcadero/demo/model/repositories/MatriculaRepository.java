package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Matricula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula , Integer> {
    Page<Matricula> findAllByMatriculaContains (String matricula, Pageable pageable);
}
