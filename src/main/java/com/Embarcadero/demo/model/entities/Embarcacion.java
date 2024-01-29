package com.Embarcadero.demo.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Embarcacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name="motor_id", referencedColumnName="id")
    private Motor motor;

    private String casco;
    private String nombre;
    private Integer capacidad;

    // @Enumerated(EnumType.STRING)
    private Tipo_Embarcacion tipo_embarcacion;

}
