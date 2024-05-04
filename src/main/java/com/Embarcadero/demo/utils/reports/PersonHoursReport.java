package com.Embarcadero.demo.utils.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonHoursReport {
    Date timePersonHours;
    int valuePersonHours;
    int valueBoatHours;
}
