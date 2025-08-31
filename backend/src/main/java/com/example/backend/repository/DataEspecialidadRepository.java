package com.example.backend.repository;

import com.example.backend.entity.DataEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataEspecialidadRepository extends JpaRepository<DataEspecialidad, Long> {
    // Por ejemplo, buscar especialidades por profesión
    List<DataEspecialidad> findByProfesionId(Long profesionId);
}
