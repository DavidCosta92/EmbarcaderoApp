package com.Embarcadero.demo.model.dtos.license;

import com.Embarcadero.demo.model.dtos.boat.RegisteredBoatAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.entities.enums.LicenseState_enum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseAddDto {
    @NotNull(message = "Matricula no puede ser nula")
    @Size(min=5, max=30, message = "Matricula debe tener entre 5 y 30 caracteres")
    private String licenseCode;

    @Valid
    private RegisteredBoatAddDto registeredBoat;

    @Valid
    private PersonAddDto owner;
    private LicenseState_enum licenseState_enum;
    private String notes;
}


