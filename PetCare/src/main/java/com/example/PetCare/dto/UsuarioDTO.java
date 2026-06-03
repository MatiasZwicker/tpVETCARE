package com.example.PetCare.dto;

import com.example.PetCare.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private Rol rol;
    private boolean activo;
}
