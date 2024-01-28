package com.Embarcadero.demo.auth.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LoguedUserDetails {
    String token;
    Integer id;
    String username;
    String lastName;
    String firstName;
    String dni;
    String phone;
    String email;
    Role role;
    Collection<? extends GrantedAuthority> authorities;
}
