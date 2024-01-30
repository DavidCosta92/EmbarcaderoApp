package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.enums.TipoMotor_enum;
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

    //@Enumerated(EnumType.STRING)
    private TipoMotor_enum tipo_motorenum;

    @Column(unique = true)
    private String numeroMotor;
    private String cilindrada;
    private String observaciones;
}
