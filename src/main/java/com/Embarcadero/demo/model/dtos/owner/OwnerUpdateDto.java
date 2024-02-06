package com.Embarcadero.demo.model.dtos.owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerUpdateDto {
    // TODO => se valida EL SERVICE YA QUE SON OPCIONALES
    private String dni;
    private String phone;
    private String emergency_phone ;
    private String address;
    private String notes;
}
