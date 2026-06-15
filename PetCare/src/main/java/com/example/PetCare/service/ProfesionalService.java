package com.example.PetCare.service;

import com.example.PetCare.dto.ProfesionalDTO;
import com.example.PetCare.enums.EstadoProfesional;
import com.example.PetCare.enums.Rol;
import com.example.PetCare.exceptions.NoEncontradoException;
import com.example.PetCare.model.Profesional;
import com.example.PetCare.repository.ProfesionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfesionalService {
    private final ProfesionalRepository profesionalRepository;

    public ProfesionalService(ProfesionalRepository profesionalRepository) {
        this.profesionalRepository = profesionalRepository;
    }

    public Profesional crear (Profesional profesional){
        return profesionalRepository.save(profesional);
    }

    public Profesional actualizar(int id, Profesional profesional){
        Profesional existente = profesionalRepository.findById(profesional.getIdUsuario())
                .orElseThrow(() -> new NoEncontradoException("El profesional no fue encontrado"));
        existente.setIdUsuario(id);
        return profesionalRepository.save(existente);
    }

    public void eliminar (int id){
        profesionalRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("El profesional no fue encontrado"));
        profesionalRepository.deleteById(id);
    }

    public List<ProfesionalDTO> listarTodosDTO(){
        return profesionalRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<ProfesionalDTO> buscarPorId(int id){
        return profesionalRepository.findById(id)
                .map(this::toDTO);
    }

    public List<ProfesionalDTO> buscarPorRol(Rol rol){
        return profesionalRepository.findByRol(rol)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ProfesionalDTO> buscarPorApellido(String apellido){
        return profesionalRepository.findByApellidoContainingIgnoreCase(apellido)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<ProfesionalDTO> buscarPorMatricula(String matricula){
        return profesionalRepository.findByMatricula(matricula)
                .map(this::toDTO);
    }

    // ==================== MÉTODOS DE APROBACIÓN ====================

    /**
     * Lista todos los profesionales que están en estado PENDIENTE.
     * Estos son los que se registraron y esperan que un admin los revise.
     */
    public List<ProfesionalDTO> buscarPendientes() {
        return profesionalRepository.findByEstado(EstadoProfesional.PENDIENTE)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Lista todos los profesionales en un estado específico (PENDIENTE, APROBADO, RECHAZADO).
     */
    public List<ProfesionalDTO> buscarPorEstado(EstadoProfesional estado) {
        return profesionalRepository.findByEstado(estado)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Aprueba un profesional: cambia su estado a APROBADO y lo activa.
     * Una vez aprobado, el profesional puede ofrecer servicios en el sistema.
     *
     * @param id ID del profesional a aprobar
     * @return true si se aprobó correctamente
     */
    public boolean aprobar(int id) {
        // Verifica que el profesional exista
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("El profesional no fue encontrado"));

        // No se puede aprobar un profesional que ya está aprobado
        if (profesional.getEstado() == EstadoProfesional.APROBADO) {
            throw new NoEncontradoException("El profesional ya está aprobado");
        }

        // Actualiza estado a APROBADO y activo a true
        return profesionalRepository.actualizarEstado(id, EstadoProfesional.APROBADO, true) > 0;
    }

    /**
     * Rechaza un profesional: cambia su estado a RECHAZADO y lo desactiva.
     * Un profesional rechazado no puede ofrecer servicios en el sistema.
     *
     * @param id ID del profesional a rechazar
     * @return true si se rechazó correctamente
     */
    public boolean rechazar(int id) {
        // Verifica que el profesional exista
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("El profesional no fue encontrado"));

        // No se puede rechazar un profesional que ya está rechazado
        if (profesional.getEstado() == EstadoProfesional.RECHAZADO) {
            throw new NoEncontradoException("El profesional ya está rechazado");
        }

        // Actualiza estado a RECHAZADO y activo a false
        return profesionalRepository.actualizarEstado(id, EstadoProfesional.RECHAZADO, false) > 0;
    }

    /**
     * Convierte una entidad Profesional a su DTO correspondiente.
     * Incluye el campo estado para que el frontend pueda mostrar el estado de aprobación.
     */
    public ProfesionalDTO toDTO(Profesional p){
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setId(p.getIdUsuario());
        dto.setNombre(p.getNombre());
        dto.setApellido(p.getApellido());
        dto.setEmail(p.getEmail());
        dto.setTelefono(p.getTelefono());
        dto.setRol(p.getRol());
        dto.setActivo(p.getActivo());
        dto.setMatricula(p.getMatricula());
        dto.setExperiencia(p.getExperiencia());
        dto.setEstado(p.getEstado());
        return dto;
    }
}
