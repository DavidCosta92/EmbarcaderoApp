package com.Embarcadero.demo.model.dtos.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDni {
    @NotNull(message = "Dni no puede ser nulo.")
    @Pattern(regexp = "\\d{8}" , message = "Dni debe ser un numero de 8 digitos.")
    String dni;
}
