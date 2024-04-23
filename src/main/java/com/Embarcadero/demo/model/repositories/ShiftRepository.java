package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.entities.enums.Dam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift , Integer> {
    Page<Shift> findAllByDamContains(Dam dam, Pageable pageable);
    Page<Shift> findAllByDateContains(Date date, Pageable pageable);
    Page<Shift> findAllByDamContainsAndDateContains(Dam dam , Date date, Pageable pageable);


    @Query("SELECT s FROM Shift s JOIN s.staff u WHERE (:dam is null or CAST(s.dam AS string) LIKE %:dam%) AND (:shiftState is null or s.close = :shiftState) AND (:year is null or YEAR(s.date) = :year) AND (:month is null or MONTH(s.date) = :month) AND (:day is null or DAY(s.date) = :day) AND (:byUser is null or u.id = :byUser)")
    Page<Shift> findAllByOptionalParametersAndUser(Dam dam , Boolean shiftState, Integer byUser, Integer year, Integer month, Integer day, Pageable pageable);


    @Query("SELECT s FROM Shift s WHERE (:dam is null or CAST(s.dam AS string) LIKE %:dam%)AND (:shiftState is null or s.close = :shiftState) AND (:year is null or YEAR(s.date) = :year) AND (:month is null or MONTH(s.date) = :month) AND (:day is null or DAY(s.date) = :day)")
    Page<Shift> findAllByOptionalParameters(Dam dam , Boolean shiftState, Integer year, Integer month, Integer day, Pageable pageable);


    @Query("SELECT s FROM Shift s JOIN s.staff u WHERE u.id = :id AND s.close = false")
    Optional<Shift> getShiftByIdUser (Integer id);

}
