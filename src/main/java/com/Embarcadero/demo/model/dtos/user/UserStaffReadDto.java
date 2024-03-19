package com.Embarcadero.demo.model.dtos.user;


import com.Embarcadero.demo.auth.entities.Role;
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
    String firstName;
    String lastName;
    Role role;
    String email;
    boolean isAccountNonExpired;
    boolean isAccountNonLocked;
    boolean isCredentialsNonExpired;
    boolean isEnabled;
    Collection<? extends GrantedAuthority> authorities;
}
