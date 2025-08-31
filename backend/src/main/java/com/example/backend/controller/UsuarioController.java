package com.example.backend.controller;

import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UsuarioRepository;
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

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    /**
     * Devuelve todos los usuarios con su estado y rol asignado.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<Usuario> usuariosList = usuarioRepository.findAll();

        List<UsuarioDTO> usuarios = usuariosList.stream().map(usuario -> {
            // Obtener roles del usuario usando el método corregido
            List<Rol> roles = rolRepository.findByUsuarios_Id(usuario.getId());
            String rolNombre = roles.isEmpty() ? "" : roles.get(0).getNombre();

            return new UsuarioDTO(
                    usuario.getId(),
                    usuario.getCorreo(),
                    usuario.isActivo(),
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
