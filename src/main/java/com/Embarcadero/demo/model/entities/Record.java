package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date startTime;

    private Date endTime;

    // deberia decir algo como un enum de estado [ activo, eliminado, salio, desconocido, siniestro, ects ]

    // TODO MANY RECORDS TO ONE private Boat boat;

    // TODO MANY RECORDS TO ONE private Person driver;
    private Integer numberOfGuests;

    private String car;

    private String notes;

}
