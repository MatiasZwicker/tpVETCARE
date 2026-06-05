package com.example.PetCare.controller;

import com.example.PetCare.model.Usuario;
import com.example.PetCare.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/buscar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id).get();
    }

    @PostMapping("/crear")
    public Usuario crear(@RequestBody Usuario dto) {
        return usuarioService.crear(dto);
    }

    @PutMapping("/actualizar/{id}")
    public Usuario actualizar(@PathVariable Integer id,@RequestBody Usuario dto) {
        return usuarioService.actualizar(dto);
    }

    //todo: agregar seguridad(tizi)
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Integer id) {
        boolean eliminado = usuarioService.eliminar(id);
    }
}
