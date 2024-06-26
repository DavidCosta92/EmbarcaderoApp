package com.Embarcadero.demo.model.dtos.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonUpdateDto {
    // TODO => se valida EL SERVICE YA QUE SON OPCIONALES
    private String dni;
    private String name;
    private String lastName;
    private String phone;
    private String emergencyPhone;
    private String address;
    private String notes;
    private Boolean isUpdate;
}
