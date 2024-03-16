package com.Embarcadero.demo.model.dtos.shift;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.dtos.user.UserDni;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftAddDto {
    @NotNull
    private Dam_enum dam;
    private List<UserDni> staff;
    private String notes;
}
