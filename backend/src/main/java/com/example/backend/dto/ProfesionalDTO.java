package com.example.backend.dto;

import java.time.LocalDate;
import java.util.List;

public record ProfesionalDTO(
        Long id,
        String nombres,
        String apellidos,
        String tipoDocumento,
        String numeroDocumento,
        String rne,
        String colegiatura,
        String especialidad,
        String telefono,
        LocalDate fechaNacimiento,
        String correoUsuario,
        boolean activoUsuario,
        List<String> roles // <-- ahora es una lista de roles
) {}
