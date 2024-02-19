package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    Page<Record> findAllByRecordStateContains(String recordState, Pageable pageable);
    Page<Record> findAllByStartTimeContains(Date startTime, Pageable pageable);
    Page<Record> findAllByRecordStateContainsAndStartTimeContains(String recordState, Date startTime, Pageable pageable);
    Page<Record> findAllByEndTimeContains(Date endTime, Pageable pageable);
    Page<Record> findAllByRecordStateContainsAndEndTimeContains(String recordState, Date endTime, Pageable pageable);
    Page<Record> findAllByRecordStateContainsAndStartTimeContainsAndEndTimeContains(String recordState, Date startTime, Date endTime, Pageable pageable);



}
