package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.boat.SimpleBoat;
import com.Embarcadero.demo.model.entities.enums.RecordState;
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
    private RecordState recordState;


    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "license", nullable = true)
    private License license;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="simpleBoat", referencedColumnName="id", nullable = true)
    private SimpleBoat simpleBoat;

    // TODO MANY RECORDS TO ONE private Person driver;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "person" , nullable = false)
    private Person person;

    private Integer numberOfGuests;

    private String car;

    private String notes;
}
