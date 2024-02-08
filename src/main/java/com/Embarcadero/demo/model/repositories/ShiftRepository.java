package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ShiftRepository extends JpaRepository<Shift , Integer> {
    Page<Shift> findAllByDamContains(Dam_enum dam, Pageable pageable);
    Page<Shift> findAllByDateContains(Date date, Pageable pageable);
    Page<Shift> findAllByDamContainsAndDateContains(Dam_enum dam , Date date, Pageable pageable);

}