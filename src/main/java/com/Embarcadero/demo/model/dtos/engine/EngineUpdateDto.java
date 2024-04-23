package com.Embarcadero.demo.model.dtos.engine;

import com.Embarcadero.demo.model.entities.enums.EngineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineUpdateDto {
    private EngineType engineType;
    private String engineNumber;
    private String cc;
    private String notes;
}
