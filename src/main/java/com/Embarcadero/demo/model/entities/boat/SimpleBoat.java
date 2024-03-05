package com.Embarcadero.demo.model.entities.boat;

import com.Embarcadero.demo.model.entities.enums.TypeSimpleBoat_enum;
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
    TypeSimpleBoat_enum typeSimpleBoat_enum;

    String details;
    String notes;

}
