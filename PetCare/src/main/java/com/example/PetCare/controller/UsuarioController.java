package com.example.PetCare.controller;

import com.example.PetCare.dto.UsuarioDTO;
import com.example.PetCare.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<UsuarioDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id).get();
    }

    @PostMapping
    public UsuarioDTO crear(@RequestBody UsuarioDTO dto) {
        boolean creado = usuarioService.crear(dto);
        if (creado) {
            return dto;
        } else {
            return null;
        }
    }

    @PutMapping("/{id}")
    public UsuarioDTO actualizar(@PathVariable Integer id,@RequestBody UsuarioDTO dto) {
        boolean actualizado = usuarioService.actualizar(id, dto);
        if (actualizado){return dto;}
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        boolean eliminado = usuarioService.eliminar(id);
    }
}
