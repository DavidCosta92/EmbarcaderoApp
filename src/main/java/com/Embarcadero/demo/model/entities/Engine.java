package com.Embarcadero.demo.model.entities;

import com.Embarcadero.demo.model.entities.enums.EngineType_enum;
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

public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EngineType_enum engineType_enum;

    @Column(unique = true)
    private String engineNumber;
    private String cc;
    private String notes;
}

