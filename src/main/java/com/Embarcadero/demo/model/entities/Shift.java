package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @Enumerated(EnumType.STRING)
    private Dam_enum dam;

    @Column(nullable = false, updatable = false)
    // @Temporal(TemporalType.DATE) // para que solo se almacene la fecha sin hora
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("recordState, startTime")
    private List<Record> records;

    // @OneToMany(fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name= "shift_staff", joinColumns = @JoinColumn(name = "shift_id"), inverseJoinColumns = @JoinColumn(name="staff_id"))
    private List<User> staff;

    private String notes;

    private Boolean close;
}
