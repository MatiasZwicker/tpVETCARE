package com.example.PetCare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;

@Entity
@Table(name = "mascota")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE mascota SET activo = false WHERE id_mascota = ?")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMascota;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private double peso;
    private Date fecha_nacimiento;
    private String observaciones;
    private boolean activo;

    @JoinColumn(name = "id_dueño")
    private Usuario usuario;
}
