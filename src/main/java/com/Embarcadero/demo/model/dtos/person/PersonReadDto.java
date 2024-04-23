package com.Embarcadero.demo.model.dtos.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonReadDto {
    private Integer id;
    private String dni;
    private String phone;
    private String name;
    private String lastName;
    private String emergencyPhone;
    private String address;
    private String notes;
}
