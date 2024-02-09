package com.Embarcadero.demo.model.dtos.staff;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StaffMemberAddDto {
    @NotNull(message = "Dni no puede ser nulo")
    @Size(min=6, max=8, message = "Dni debe tener entre 6 y 8 caracteres")
    private String dni;
}
