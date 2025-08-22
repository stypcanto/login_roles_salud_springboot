package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {}
