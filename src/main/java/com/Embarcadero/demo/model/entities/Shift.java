package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date DATE;

    // lista de ingresos

    // TODO Lista usuarios que estan de guardia, deben ser guardavidas..  relacion many to many



}
