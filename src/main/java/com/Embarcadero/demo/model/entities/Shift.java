package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.entities.enums.Dam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private Dam dam;

    @Column(nullable = false, updatable = false)
    // @Temporal(TemporalType.DATE) // para que solo se almacene la fecha sin hora
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("startTime, recordState")
    private List<Record> records;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name= "shift_staff", joinColumns = @JoinColumn(name = "shift"), inverseJoinColumns = @JoinColumn(name="staff"))
    private List<User> staff;

    private String notes;

    private Boolean close;

    public Integer getTotalBoats(){
        return records.size();
    }
    public Integer getTotalPersons(){
        Integer totalPersons=0;
        for(int i =0; i< records.size() ; i++){
            totalPersons+=records.get(i).getNumberOfGuests()+1;
        }
        return totalPersons;
    }
}
