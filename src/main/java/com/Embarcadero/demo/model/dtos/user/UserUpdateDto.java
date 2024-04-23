package com.Embarcadero.demo.model.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserUpdateDto {
    String phone;
    String emergencyPhone;
    String firstName;
    String lastName;
}
