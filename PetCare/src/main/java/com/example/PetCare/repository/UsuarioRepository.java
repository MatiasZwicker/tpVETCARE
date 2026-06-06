package com.example.PetCare.repository;

import com.example.PetCare.enums.Rol;
import com.example.PetCare.model.Usuario;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByRol(Rol role);
    List<Usuario> findByNombre(String nombre);
    List<Usuario> findByEmail(String email);
    List<Usuario> findByTelefono(String telefono);
    List<Usuario> findByDireccion(String direccion);
    List<Usuario> findByActivo(boolean activo);
}
