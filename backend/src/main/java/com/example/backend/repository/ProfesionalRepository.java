package com.example.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entity.Profesional;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
    Optional<Profesional> findByUsuarioId(Long usuarioId);
}
