package com.Embarcadero.demo.auth.entities;

import jakarta.validation.constraints.Email;
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

public class RegisterRequest {
    @NotNull(message = "Username no puede ser nulo")
    @Size(min=2, max=30, message = "Username debe tener entre 2 y 30 caracteres")
    String username;

    @NotNull(message = "Password no puede ser nulo")
    @Size(min=5, max=30, message = "Password debe tener entre 5 y 30 caracteres")
    String password1;

    @NotNull(message = "Password no puede ser nulo")
    @Size(min=5, max=30, message = "Password debe tener entre 5 y 30 caracteres")
    String password2;

    @NotNull(message = "Nombre no puede ser nulo")
    @Size(min=2, max=30, message = "Nombre debe tener entre 2 y 30 caracteres")
    String firstName;

    @NotNull(message = "Apellido no puede ser nulo")
    @Size(min=2, max=30, message = "Apellido debe tener entre 2 y 30 caracteres")
    String lastName;

    @NotNull(message = "Dni no puede ser nulo")
    @Size(min=8, max=10, message = "Dni debe tener entre 8 Y 10 caracteres")
    String dni;

    @NotNull(message = "Telefono no puede ser nulo")
    @Size(min=9, max=14, message = "Telefono debe tener entre 9 y 14 caracteres")
    String phone;

    @NotNull(message = "Telefono de emergencia no puede ser nulo")
    @Size(min=9, max=14, message = "Telefono debe tener entre 9 y 14 caracteres")
    String emergency_phone;

    @NotNull(message = "Email no puede ser nulo")
    @Email(message = "Email con formato invalido")
    String email;
}
