package com.Embarcadero.demo.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculasArrayDto {
    private List matriculas;
    private Long total_results;
    private Integer results_per_page;
    private Integer current_page;
    private Integer pages;
    private String sort_by;
}
