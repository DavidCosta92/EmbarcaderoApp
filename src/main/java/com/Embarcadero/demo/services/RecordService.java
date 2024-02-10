package com.Embarcadero.demo.services;

import com.Embarcadero.demo.model.mappers.RecordMapper;
import com.Embarcadero.demo.model.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private RecordMapper recordMapper;


}
