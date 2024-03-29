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
    // ESTAS VALIDACIONES ESTAN COMENTADAS, PORQUE EN CASO DE UN OWNER YA EXISTENTE, SOLO SE ENVIA DNI Y NO HACE FALTA ENVIAR EL RESTO DE DATOS...
    // @NotNull(message = "Telefono no puede ser nulo.")
    // @Size(min = 7, max = 14, message = "Telefono debe ser un numero de 7 a 14 digitos.")
    private String phone;
    // ESTAS VALIDACIONES ESTAN COMENTADAS, PORQUE EN CASO DE UN OWNER YA EXISTENTE, SOLO SE ENVIA DNI Y NO HACE FALTA ENVIAR EL RESTO DE DATOS...
    // @NotNull(message = "Telefono de emergencia  no puede ser nulo.")
    // @Size(min = 7, max = 14, message = "Telefono de emergencia debe ser un numero de 7 a 14 digitos.")
    private String emergency_phone ;

    private String name;

    private String lastName;

    // ESTAS VALIDACIONES ESTAN COMENTADAS, PORQUE EN CASO DE UN OWNER YA EXISTENTE, SOLO SE ENVIA DNI Y NO HACE FALTA ENVIAR EL RESTO DE DATOS...
    // @NotNull(message = "Direccion no puede ser nula.")
    // @NotBlank( message = "Direccion no puede estar vacia")
    private String address;

    private String notes;

    public PersonAddDto (String dni){
        // este constructor es para aquellos casos donde solo envio el dni, en el caso de agregar una persona a un record
        this.dni = dni;
    }
}