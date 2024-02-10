package com.Embarcadero.demo.model.dtos.shift;

import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftUpdateDto {
    private Dam_enum dam;
    // date NO ES MODIFICABLE POR REGLA DE NEGOCIO
    // records tiene su propio endpoint para modificar
    // staff tiene su propio endpoint para modificar
    private String notes;
}
