package com.example.PetCare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaDTO {
    private int idMascota;

    @NotBlank
    private String nombre;

    @NotBlank
    private String especie;

    private String raza;
    private String sexo;

    @Positive
    private double peso;

    private LocalDate fecha_nacimiento;
    private String observaciones;
    private boolean activo;

    @NotNull
    private Integer idUsuario;
}
