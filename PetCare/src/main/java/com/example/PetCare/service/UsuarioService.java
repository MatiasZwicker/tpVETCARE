package com.example.PetCare.service;

import com.example.PetCare.enums.Rol;
import com.example.PetCare.model.Usuario;
import com.example.PetCare.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    public List<Usuario> buscarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    public Usuario crear(Usuario entity) {
        return usuarioRepository.save(entity);
    }

    public List<Usuario> findByNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    public List<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> findByTelefono(String telefono) {
        return usuarioRepository.findByTelefono(telefono);
    }

    public List<Usuario> findByDireccion(String direccion) {
        return usuarioRepository.findByDireccion(direccion);
    }

    public List<Usuario> findByActivo(boolean activo) {
        return usuarioRepository.findByActivo(activo);
    }

    public Usuario actualizar(Usuario entity) {
        Usuario usu = usuarioRepository.findById(entity.getIdUsuario()).orElse(null);
        usu.setNombre(entity.getNombre());
        usu.setApellido(entity.getApellido());
        usu.setEmail(entity.getEmail());
        usu.setTelefono(entity.getTelefono());
        usu.setDireccion(entity.getDireccion());
        usu.setTelefono(entity.getTelefono());
        usu.setActivo(true);
        return usu;

    }

    public boolean eliminar(Integer idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            usuarioRepository.deleteById(idUsuario);
            return true;
        }
        return false;
    }
}
