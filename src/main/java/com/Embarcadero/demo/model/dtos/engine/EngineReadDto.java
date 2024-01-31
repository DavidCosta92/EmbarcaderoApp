package com.Embarcadero.demo.model.dtos.engine;

import com.Embarcadero.demo.model.entities.enums.EngineType_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineReadDto {
    private Integer id;
    private EngineType_enum engineType_enum;
    private String engineNumber;
    private String cc;
    private String notes;
}
