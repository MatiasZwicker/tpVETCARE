package com.example.PetCare.controller;

import com.example.PetCare.dto.TurnoDTO;
import com.example.PetCare.model.Turno;
import com.example.PetCare.service.TurnoService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @GetMapping
    public List<TurnoDTO> listarTodos() {
        return turnoService.listarTodos();
    }

    @GetMapping("/activos")
    public List<TurnoDTO> listarActivos() {
        return turnoService.listarTodosTurnosActivos();
    }

    @GetMapping("/mascota/{idMascota}")
    public List<TurnoDTO> listarPorMascota(@PathVariable Integer idMascota) {
        return turnoService.listarTurnoXMascota(idMascota);
    }

    @GetMapping("/profesional/{idProfesional}")
    public List<TurnoDTO> listarPorProfesional(@PathVariable Integer idProfesional) {
        return turnoService.listarTurnoXProfesional(idProfesional);
    }

    @GetMapping("/fecha/antes/{fecha}")
    public List<TurnoDTO> listarAntesDe(@PathVariable LocalDate fecha) {
        return turnoService.listarFechaBefore(fecha);
    }

    @GetMapping("/fecha/despues/{fecha}")
    public List<TurnoDTO> listarDespuesDe(@PathVariable LocalDate fecha) {
        return turnoService.listarFechaAfter(fecha);
    }

    @GetMapping("/fecha/{fecha}")
    public List<TurnoDTO> listarPorFecha(@PathVariable LocalDate fecha) {
        return turnoService.listarFechaMascota(fecha);
    }

    @PostMapping
    public TurnoDTO crear(@RequestBody TurnoDTO dto) {
        boolean creado = turnoService.crear(dto);
        if (creado) return dto;
        return null;
    }

    @PutMapping("/{id}")
    public Turno actualizar(@PathVariable Integer id, @RequestBody TurnoDTO dto) {
        return turnoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        turnoService.eliminar(id);
    }

    @PutMapping("/{id}/cancelar")
    public int cancelarTurno(@PathVariable Integer id) {
        return turnoService.cancelaTurno(id);
    }

    @PutMapping("/{id}/confirmar")
    public int confirmarTurno(@PathVariable Integer id) {
        return turnoService.confirmarTurno(id);
    }
}
