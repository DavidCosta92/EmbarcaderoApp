package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.enums.TipoEmbarcacion_enum;
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

    @Column(unique = true)
    private String nombre;

    private Integer capacidad;

    // @Enumerated(EnumType.STRING)
    private TipoEmbarcacion_enum tipo_embarcacionEnum;

}
