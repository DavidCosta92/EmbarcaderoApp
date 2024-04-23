package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    @Query("SELECT r FROM Record r WHERE (:recordState is null or CAST(r.recordState AS string) LIKE %:recordState%) AND (:sYear is null or YEAR(r.startTime) = :sYear) AND (:sMonth is null or MONTH(r.startTime) = :sMonth) AND (:sDay is null or DAY(r.startTime) = :sDay)  AND (:eYear is null or YEAR(r.endTime) = :eYear) AND (:eMonth is null or MONTH(r.endTime) = :eMonth) AND (:eDay is null or DAY(r.endTime) = :eDay)")
    Page<Record> findAllByOptionalParameters(RecordState recordState, Integer sYear, Integer sMonth, Integer sDay, Integer eYear, Integer eMonth, Integer eDay, Pageable pageable);
}
