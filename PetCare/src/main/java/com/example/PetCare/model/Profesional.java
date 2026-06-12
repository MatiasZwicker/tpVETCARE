package com.example.PetCare.model;

import com.example.PetCare.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesional extends Usuario {
    @NotBlank
    private String matricula;

    @NotBlank
    private String experiencia;
}
