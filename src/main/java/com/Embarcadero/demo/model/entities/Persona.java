package com.Embarcadero.demo.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false , unique = true)
    private String dni;

    private String telefono;
    private String telefono_emergencia;
    private String direccion;
    private String observaciones;

    @OneToMany(mappedBy = "duenio")
    private Set<Matricula> matriculas;
}
