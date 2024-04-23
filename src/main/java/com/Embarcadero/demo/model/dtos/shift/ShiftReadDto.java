package com.Embarcadero.demo.model.dtos.shift;

import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.user.UserReadDto;
import com.Embarcadero.demo.model.entities.enums.Dam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftReadDto {
    private Integer id;
    private Dam dam;
    private LocalDate date;
    // TODO Lista usuarios que estan de guardia, deben ser guardavidas..  relacion many to many
    private List<RecordReadDto> records;
    private List<UserReadDto> staff;
    private String notes;
    private Boolean close;
}
