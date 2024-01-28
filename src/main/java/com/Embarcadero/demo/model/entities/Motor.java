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

public class Motor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "motor")
    private Embarcacion embarcacion;

    //@Enumerated(EnumType.STRING)
    private Tipo_motor tipo_motor;
    private String numero_motor;
    private String cilindrada;
    private String observaciones;
}
