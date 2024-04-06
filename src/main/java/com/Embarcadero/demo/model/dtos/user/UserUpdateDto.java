package com.Embarcadero.demo.model.dtos.user;

import com.Embarcadero.demo.auth.entities.Role;
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
    String emergency_phone;
    String firstName;
    String lastName;
}
