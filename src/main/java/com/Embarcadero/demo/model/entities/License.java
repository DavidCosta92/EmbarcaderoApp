package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.enums.LicenseState;
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

public class License {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false , unique = true)
    private String code;

    @OneToOne
    @JoinColumn(name="registeredBoat", referencedColumnName="id")
    private RegisteredBoat registeredBoat;

    @ManyToOne()
    @JoinColumn(name = "owner" , nullable = false)
    private Person owner;

    @Enumerated(EnumType.STRING)
    private LicenseState state;

    private String notes;
}
