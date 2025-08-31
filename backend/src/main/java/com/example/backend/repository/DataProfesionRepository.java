package com.example.backend.repository;

import com.example.backend.entity.DataProfesion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataProfesionRepository extends JpaRepository<DataProfesion, Long> {
    // Puedes agregar métodos de búsqueda personalizados si los necesitas
    // Por ejemplo:
    // Optional<DataProfesion> findByNombre(String nombre);
}
