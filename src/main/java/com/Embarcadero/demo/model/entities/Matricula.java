package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.enums.Estado_enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class Matricula {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false , unique = true)
    private String matricula;

    @OneToOne
    @JoinColumn(name="embarcacion_id", referencedColumnName="id")
    private Embarcacion embarcacion;


    @ManyToOne()
    @JoinColumn(name = "id_duenio_fk" , nullable = false)
    private Persona duenio;

    private Estado_enum estadoEnum;
}
