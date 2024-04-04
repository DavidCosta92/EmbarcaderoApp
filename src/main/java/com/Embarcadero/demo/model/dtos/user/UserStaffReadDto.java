package com.Embarcadero.demo.model.dtos.user;


import com.Embarcadero.demo.auth.entities.Role;
import com.Embarcadero.demo.model.entities.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStaffReadDto {
    Integer id;
    String username;
    String dni;
    String phone;
    String emergency_phone;
    String firstName;
    String lastName;
    Role role;
    String email;
    ImageFile profileImage;
    boolean isAccountNonExpired;
    boolean isAccountNonLocked;
    boolean isCredentialsNonExpired;
    boolean isEnabled;
    Collection<? extends GrantedAuthority> authorities;
}
