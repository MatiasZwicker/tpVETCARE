package com.example.PetCare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaDTO {
    private int idMascota;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private double peso;
    private Date fecha_nacimiento;
    private String observaciones;
    private boolean activo;
    private int idUsuario;

}
