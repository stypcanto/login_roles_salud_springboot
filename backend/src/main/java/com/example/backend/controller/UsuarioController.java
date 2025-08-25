package com.example.backend.controller;

import com.example.backend.entity.Rol;
import com.example.backend.entity.User;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para exponer usuarios junto con sus roles.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public UsuarioController(UserRepository userRepository, RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    /**
     * Devuelve todos los usuarios con su estado y rol asignado.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<User> users = userRepository.findAll();

        List<UsuarioDTO> usuarios = users.stream().map(user -> {
            // Obtener roles del usuario
            List<Rol> roles = rolRepository.findRolesByUsuarioId(user.getId());
            String rolNombre = roles.isEmpty() ? "" : roles.get(0).getNombre(); // tomamos el primero si hay más

            return new UsuarioDTO(
                    user.getId(),
                    user.getCorreo(),
                    user.isActivo(),
                    rolNombre
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    // ================= DTO =================
    public record UsuarioDTO(
            Long id,
            String correo,
            boolean activo,
            String rol
    ) {}
}
