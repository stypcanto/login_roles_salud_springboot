package com.example.backend.controller;

import com.example.backend.dto.UsuarioDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Permite llamadas desde frontend (React)
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getCorreo(),
                        usuario.isActivo(),
                        usuario.getRoles() != null
                                ? usuario.getRoles().stream().map(r -> r.getNombre()).toList()
                                : List.of() // si no tiene roles, retornamos lista vacía
                ))
                .toList(); // usamos toList() directamente

        return ResponseEntity.ok(usuarios);
    }
}
