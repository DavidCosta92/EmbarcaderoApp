package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.enums.LicenseState_enum;
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
    private String licenseCode;

    @OneToOne
    @JoinColumn(name="registeredBoat_id", referencedColumnName="id")
    private RegisteredBoat registeredBoat;

    @ManyToOne()
    @JoinColumn(name = "id_owner_fk" , nullable = false)
    private Person owner;

    @Enumerated(EnumType.STRING)
    private LicenseState_enum licenseState_enum;

    private String notes;
}
