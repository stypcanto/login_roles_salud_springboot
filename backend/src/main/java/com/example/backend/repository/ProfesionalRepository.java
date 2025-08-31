package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entity.Profesional;
import com.example.backend.entity.Usuario;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {

    // Buscar un profesional por su usuario
    Optional<Profesional> findByUsuario(Usuario usuario);

    // Buscar profesionales por especialidad
    List<Profesional> findByEspecialidadId(Long especialidadId);
}
