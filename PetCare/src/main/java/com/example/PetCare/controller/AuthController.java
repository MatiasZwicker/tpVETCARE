package com.example.PetCare.controller;

import com.example.PetCare.dto.RegistroRequest;
import com.example.PetCare.model.Usuario;
import com.example.PetCare.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public AuthController(UserDetailsManager userDetailsManager,
                          PasswordEncoder passwordEncoder,
                          UsuarioRepository usuarioRepository) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registrar(@RequestBody @Valid RegistroRequest request) {
        if (userDetailsManager.userExists(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El email ya está registrado"));
        }

        String hash = passwordEncoder.encode(request.getPassword()); // Hashea la pass

        // userDetailsManager.createUser() — Guarda el usuario en la tabla users y el rol en la tabla authorities.
        // El .roles("USER") se transforma automáticamente en ROLE_USER en la tabla.
        userDetailsManager.createUser(
            User.builder()
                .username(request.getEmail())
                .password(hash)
                .roles(request.getRol().name())
                .build()
        );

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(request.getRol());
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado exitosamente"));
    }
}