package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.entity.Profesional;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import com.example.backend.repository.ProfesionalRepository;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UsuarioRepository;

@Service
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ProfesionalRepository profesionalRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       RolRepository rolRepository,
                       ProfesionalRepository profesionalRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.profesionalRepository = profesionalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Profesional getProfesionalByUsuarioId(Long usuarioId) {
        Usuario usuario = findById(usuarioId);
        return profesionalRepository.findByUsuario(usuario).orElse(null);
    }

    public List<Rol> getRoles(Long usuarioId) {
        Usuario usuario = findById(usuarioId);
        return List.copyOf(usuario.getRoles());
    }

    public void saveProfesional(Profesional profesional) {
        profesionalRepository.save(profesional);
    }

    public void assignRole(Long usuarioId, String rolNombre) {
        Usuario usuario = findById(usuarioId);
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
    }

    public Usuario registerAndReturnUser(String correo, String contrasena) {
        Optional<Usuario> existing = usuarioRepository.findByCorreo(correo);
        if (existing.isPresent()) return null;

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    private Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
