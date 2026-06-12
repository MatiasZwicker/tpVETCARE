package com.example.PetCare.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReseñaProfesionalDTO {
    private Integer id;

    @NotBlank
    @Size(max = 500)
    private String texto;

    @Min(1)
    @Max(5)
    private Integer puntuacion;

    @NotNull
    @PastOrPresent
    private LocalDate fecha;

    private Boolean activo;

    @NotNull
    private Integer id_usuario;

    @NotNull
    private Integer id_profesional;
}
