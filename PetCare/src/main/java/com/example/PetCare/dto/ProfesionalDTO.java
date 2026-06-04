package com.example.PetCare.dto;

import com.example.PetCare.enums.Estado_Turno;
import com.example.PetCare.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesionalDTO {
    private int id;
    private String matricula;
    private Rol rol;
    private String experiencia;
    private Estado_Turno estado;
    private int id_usuario;
}
