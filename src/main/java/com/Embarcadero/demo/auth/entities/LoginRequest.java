package com.Embarcadero.demo.auth.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotNull(message = "Username no puede ser nulo")
    @Size(min=2, max=30, message = "Username debe tener entre 2 y 30 caracteres")
    String username;

    @NotNull(message = "Password no puede ser nulo")
    @Size(min=2, max=30, message = "Password debe tener entre 2 y 30 caracteres")
    String password;
}
