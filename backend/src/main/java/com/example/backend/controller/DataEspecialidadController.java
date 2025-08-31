package com.example.backend.controller;

import com.example.backend.entity.DataEspecialidad;
import com.example.backend.repository.DataEspecialidadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class DataEspecialidadController {

    private final DataEspecialidadRepository repository;

    public DataEspecialidadController(DataEspecialidadRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<DataEspecialidad>> getAllEspecialidades() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/por-profesion/{profesionId}")
    public ResponseEntity<List<DataEspecialidad>> getEspecialidadesByProfesion(@PathVariable Long profesionId) {
        return ResponseEntity.ok(repository.findByProfesionId(profesionId));
    }
}
