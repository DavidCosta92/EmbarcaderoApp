package com.Embarcadero.demo.model.entities.boat;

import com.Embarcadero.demo.model.entities.Engine;
import com.Embarcadero.demo.model.entities.enums.TypeLicencedBoat;
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
    @JoinColumn(name="motor", referencedColumnName="id", nullable = true)
    private Engine engine;

    private String hull;

    private String name;

    private Integer capacity;

    private String details;

    @Enumerated(EnumType.STRING)
    private TypeLicencedBoat typeLicencedBoat;
}
