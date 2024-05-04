package com.Embarcadero.demo.utils.reports;

import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.model.entities.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordDtoReport {
    private Integer id;
    private Date startTime;
    private Date endTime;
    private String boatType;
    private String personDni;
    private String personPhone;
    private Integer numberOfGuests;
    private String car;
    private String recordState;
    private String licenseCode;
}
