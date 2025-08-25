package com.example.backend.repository;

import com.example.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    // Obtener todos los roles de un usuario por su ID
    @Query("SELECT r FROM Rol r JOIN r.usuarios u WHERE u.id = :usuarioId")
    List<Rol> findRolesByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Buscar rol por nombre
    Optional<Rol> findByNombre(String nombre);
}
