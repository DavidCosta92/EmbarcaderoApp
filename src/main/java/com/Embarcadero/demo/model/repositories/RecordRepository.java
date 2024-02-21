package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    @Query("SELECT r FROM Record r WHERE CAST(r.recordState AS string) LIKE %:recordState%")
    Page<Record> findAllByRecordStateContains(RecordState_enum recordState, Pageable pageable);
    @Query("SELECT r FROM Record r WHERE CAST(r.startTime AS string) LIKE %:startTime%")
    Page<Record> findAllByStartTimeContains(Date startTime, Pageable pageable);
    Page<Record> findAllByRecordStateContainsAndStartTimeContains(RecordState_enum recordState, Date startTime, Pageable pageable);
    Page<Record> findAllByEndTimeContains(Date endTime, Pageable pageable);
    Page<Record> findAllByRecordStateContainsAndEndTimeContains(RecordState_enum recordState, Date endTime, Pageable pageable);
    Page<Record> findAllByRecordStateContainsAndStartTimeContainsAndEndTimeContains(RecordState_enum recordState, Date startTime, Date endTime, Pageable pageable);
}
