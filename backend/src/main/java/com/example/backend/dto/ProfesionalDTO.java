package com.example.backend.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para enviar datos de profesional al frontend.
 * Combina información del profesional, usuario y roles.
 */
public record ProfesionalDTO(
        Long id,
        String nombres,
        String apellidos,
        String tipoDocumento,
        String numeroDocumento,
        String rne,
        String colegiatura,
        String especialidadNombre,  // nombre de la especialidad
        String telefono,
        LocalDate fechaNacimiento,
        String correoUsuario,
        boolean activoUsuario,
        List<String> roles          // roles del usuario
) {}
