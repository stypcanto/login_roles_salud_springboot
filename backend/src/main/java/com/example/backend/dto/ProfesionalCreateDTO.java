package com.example.backend.dto;

import java.time.LocalDate;

public record ProfesionalCreateDTO(
        String nombres,
        String apellidos,
        String tipoDocumento,
        String numeroDocumento,
        String rne,
        String colegiatura,
        String especialidad,
        String telefono,
        LocalDate fechaNacimiento,
        String correoUsuario // para asociar al usuario existente
) {}
