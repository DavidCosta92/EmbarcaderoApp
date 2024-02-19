package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.enums.State_enum;
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
    @JoinColumn(name="boat_id", referencedColumnName="id")
    private Boat boat;


    @ManyToOne()
    @JoinColumn(name = "id_owner_fk" , nullable = false)
    private Person person;

    @Enumerated(EnumType.STRING)
    private State_enum state_enum;
}
