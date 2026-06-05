package com.example.PetCare.model;

import com.example.PetCare.enums.Estado_Turno;
import com.example.PetCare.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesional extends Usuario{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String matricula;
    private String experiencia;
}
