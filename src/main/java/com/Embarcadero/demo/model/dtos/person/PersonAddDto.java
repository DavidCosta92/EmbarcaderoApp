package com.Embarcadero.demo.model.dtos.person;

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
public class PersonAddDto {
    @NotNull(message = "Dni no puede ser nulo.")
    @Pattern(regexp = "\\d{8}" , message = "Dni debe ser un numero de 8 digitos.")  //Que sea cualquier dígito decimal equivalente a [0-9] => ("d") x8 veces => ("{8}")
    private String dni;
    private String phone;
    private String emergencyPhone;
    private String name;
    private String lastName;
    private String address;

    private String notes;

    public PersonAddDto (String dni){
        // este constructor es para aquellos casos donde solo envio el dni, en el caso de agregar una persona a un record
        this.dni = dni;
    }
}