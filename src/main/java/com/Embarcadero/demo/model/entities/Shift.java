package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Dam_enum dam;

    @Column(nullable = false, updatable = false)
    private Date date;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Record> records;

    //@OneToMany(fetch = FetchType.LAZY)
    // private List<Person> staff;

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> staff;

    private String notes;

    private Boolean close;
}
