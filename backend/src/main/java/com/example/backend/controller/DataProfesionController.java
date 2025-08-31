package com.example.backend.controller;

import com.example.backend.entity.DataProfesion;
import com.example.backend.repository.DataProfesionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesiones")
public class DataProfesionController {

    private final DataProfesionRepository repository;

    public DataProfesionController(DataProfesionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<DataProfesion>> getAllProfesiones() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataProfesion> getProfesionById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
