package com.example.backend.dto;

import java.time.LocalDate;

/**
 * DTO para crear o actualizar un profesional.
 * Incluye referencias por ID a Profesion y Especialidad.
 */
public record ProfesionalCreateDTO(
        String nombres,
        String apellidos,
        String tipoDocumento,
        String numeroDocumento,
        String rne,
        String colegiatura,
        Long profesionId,        // referencia a DataProfesion
        Long especialidadId,     // referencia a DataEspecialidad (opcional)
        String telefono,
        LocalDate fechaNacimiento,
        String correoUsuario     // para asociar al usuario existente
) {}

