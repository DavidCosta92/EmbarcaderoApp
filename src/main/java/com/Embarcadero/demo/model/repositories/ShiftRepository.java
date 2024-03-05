package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift , Integer> {
    Page<Shift> findAllByDamContains(Dam_enum dam, Pageable pageable);
    Page<Shift> findAllByDateContains(Date date, Pageable pageable);
    Page<Shift> findAllByDamContainsAndDateContains(Dam_enum dam , Date date, Pageable pageable);


    @Query("SELECT s FROM Shift s WHERE (:dam is null or CAST(s.dam AS string) LIKE %:dam%) AND (:year is null or YEAR(s.date) = :year) AND (:month is null or MONTH(s.date) = :month) AND (:day is null or DAY(s.date) = :day)")
    Page<Shift> findAllByOptionalParameters(Dam_enum dam , Integer year, Integer month, Integer day, Pageable pageable);


    @Query("SELECT s from FROM Shift s JOIN s.staff u WHERE u.id = :id")
    Optional<Shift> getShiftByIdUser (Integer id);

}
