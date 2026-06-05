package com.example.PetCare.service;

import com.example.PetCare.model.Profesional;
import com.example.PetCare.repository.ProfesionalRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfesionalService {
    private final ProfesionalRepository profesionalRepository;

    public ProfesionalService(ProfesionalRepository profesionalRepository) {
        this.profesionalRepository = profesionalRepository;
    }

    public Profesional crear (Profesional profesional){
        return profesionalRepository.save(profesional);
    }
}
