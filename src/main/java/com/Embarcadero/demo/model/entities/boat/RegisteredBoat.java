package com.Embarcadero.demo.model.entities.boat;

import com.Embarcadero.demo.model.entities.Engine;
import com.Embarcadero.demo.model.entities.enums.TypeLicencedBoat_enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisteredBoat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name="motor_id", referencedColumnName="id", nullable = true)
    private Engine engine;

    private String hull;

    private String name;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private TypeLicencedBoat_enum typeLicencedBoat_enum;
}
