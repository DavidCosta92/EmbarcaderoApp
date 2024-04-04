package com.Embarcadero.demo.model.dtos.user;

import com.Embarcadero.demo.auth.entities.Role;
import com.Embarcadero.demo.model.entities.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReadDto {
    Integer id;
    String username;
    String dni;
    String phone;
    String emergency_phone;
    String firstName;
    String lastName;
    String email;
    Role role;
    ImageFile profileImage;
}
