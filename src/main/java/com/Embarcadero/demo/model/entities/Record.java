package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.boat.SimpleBoat;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private RecordState_enum recordState;


    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "license_id", nullable = true)
    private License license;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="simpleBoat_id", referencedColumnName="id", nullable = true) // TODO, PONER QUE AL BORRAR, BORRE EN CASCADA ASI LIMPO BD!
    private SimpleBoat simpleBoat;

    // TODO MANY RECORDS TO ONE private Person driver;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "person_id" , nullable = false)
    private Person person;

    private Integer numberOfGuests;

    private String car;

    private String notes;
}
