package com.Embarcadero.demo.auth;

import com.Embarcadero.demo.auth.entities.Role;
import com.Embarcadero.demo.auth.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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



    /*
    @Query("SELECT r FROM Record r WHERE (:recordState is null or CAST(r.recordState AS string) LIKE %:recordState%) AND (:sYear is null or YEAR(r.startTime) = :sYear) AND (:sMonth is null or MONTH(r.startTime) = :sMonth) AND (:sDay is null or DAY(r.startTime) = :sDay)  AND (:eYear is null or YEAR(r.endTime) = :eYear) AND (:eMonth is null or MONTH(r.endTime) = :eMonth) AND (:eDay is null or DAY(r.endTime) = :eDay)")
    Page<Record> findAllByOptionalParameters(RecordState_enum recordState, Integer sYear, Integer sMonth, Integer sDay, Integer eYear, Integer eMonth, Integer eDay, Pageable pageable);

       @Query("SELECT s FROM Shift s WHERE (:dam is null or CAST(s.dam AS string) LIKE %:dam%) AND (:year is null or YEAR(s.date) = :year) AND (:month is null or MONTH(s.date) = :month) AND (:day is null or DAY(s.date) = :day)")
    Page<Shift> findAllByOptionalParameters(Dam_enum dam , Integer year, Integer month, Integer day, Pageable pageable);

     */
    @Query("SELECT u FROM users u WHERE ( CAST(u.role AS string) LIKE %:role% AND u.lastName LIKE %:fullName% OR u.firstName LIKE %:fullName%)")
    Page<User> getAllByRoleAndFullName (Role role, String fullName, Pageable pageable);

    Page<User> findAllByRole (Role role, Pageable pageable);


}
