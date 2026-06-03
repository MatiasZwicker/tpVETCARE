package com.example.PetCare.Service;

import com.example.PetCare.model.RolUsuario;
import com.example.PetCare.model.Usuario;
import com.example.PetCare.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Función para registrar un usuario común desde el formulario público.
    // Solo permite crear usuarios con rol USUARIO. Rechaza cualquier intento de registro
    // con roles profesionales (VETERINARIO, PASEADOR, etc.) o ADMINISTRADOR.
    // Verifica que el email no esté duplicado. La contraseña se guarda hasheada con BCrypt.
    // - Matias Z.
    public Usuario registrar(String nombre, String apellido, String email, String password, RolUsuario rol) {
        if (rol != RolUsuario.USUARIO) {
            throw new IllegalArgumentException("El registro público solo está disponible para usuarios. Los profesionales deben postularse.");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado en  el sistema");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol(rol);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    /* Función para autenticar un usuario al iniciar sesión.*/
    // Busca al usuario por email, verifica que esté activo, que la contraseña
    // coincida y que el rol seleccionado en el formulario
    // sea exactamente el mismo que tiene asignado en la base de datos.
    // - Matias Z.
    public Usuario autenticar(String email, String password, RolUsuario rol) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email no registrado"));

        if (!usuario.isActivo()) {
            throw new IllegalArgumentException("Usuario desactivado");
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta. Intente nuevamente");
        }

        if (usuario.getRol() != rol) {
            throw new IllegalArgumentException("El rol seleccionado no coincide");
        }

        return usuario;
    }

    // Crea un usuario profesional cuando el administrador aprueba una postulación.
    // Asigna el rol solicitado, una contraseña default (PetCare123) y marca al
    // usuario para que deba cambiarla en su primer inicio de sesión.
    // - Matias Z.
    public Usuario crearUsuarioProfesional(String nombre, String apellido, String email, RolUsuario rol) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado en el sistema");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode("PetCare123"));
        usuario.setRol(rol);
        usuario.setActivo(true);
        usuario.setDebeCambiarPassword(true); //bandera que indica que el profesional aun debe cambiar la contraseña

        return usuarioRepository.save(usuario);
    }

    // Cambia la contraseña de un usuario y desactiva la bandera debeCambiarPassword.
    // Se usa cuando un profesional inicia sesión por primera vez con la contraseña default.
    // - Matias Z.
    public void cambiarPassword(Usuario usuario, String nuevaPassword) {
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuario.setDebeCambiarPassword(false);
        usuarioRepository.save(usuario);
    }
}
