package com.example.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(String nombre);

    // Buscar roles asociados a un usuario por su id
    List<Rol> findByUsuarios_Id(Long usuarioId);
}
