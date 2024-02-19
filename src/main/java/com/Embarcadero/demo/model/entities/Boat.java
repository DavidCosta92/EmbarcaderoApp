package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.enums.TypeBoat_enum;
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

public class Boat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name="motor_id", referencedColumnName="id", nullable = true)
    private Engine engine;

    private String hull;

    @Column(unique = true)
    private String name;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private TypeBoat_enum typeBoat_enum;

}
