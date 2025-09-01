package com.example.backend.repository;

import com.example.backend.entity.Profesional;
import com.example.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {

    // Buscar un profesional por su usuario
    Optional<Profesional> findByUsuario(Usuario usuario);

    // Buscar profesional por el ID de usuario
    Optional<Profesional> findByUsuario_Id(Long usuarioId);

    // Buscar profesionales por especialidad
    List<Profesional> findByEspecialidadId(Long especialidadId);
}
