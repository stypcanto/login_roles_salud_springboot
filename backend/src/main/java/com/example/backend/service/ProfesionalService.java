package com.example.backend.service;

import java.util.List;
import com.example.backend.entity.Profesional;

public interface ProfesionalService {

    List<Profesional> getAllProfesionales();

    Profesional getProfesionalById(Long id);

    Profesional getProfesionalByUsuarioId(Long usuarioId);

    List<Profesional> getProfesionalesByEspecialidad(Long especialidadId);

    Profesional saveProfesional(Profesional profesional);

    void deleteProfesional(Long id);
}
