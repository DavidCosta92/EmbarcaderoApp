package com.Embarcadero.demo.model.entities.boat;

import com.Embarcadero.demo.model.entities.enums.TypeSimpleBoat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SimpleBoat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Enumerated(EnumType.STRING)
    TypeSimpleBoat typeSimpleBoat;

    String details;
    String notes;

}
