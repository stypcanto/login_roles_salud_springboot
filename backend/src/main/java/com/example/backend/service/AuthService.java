package com.example.backend.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.entity.Profesional;
import com.example.backend.entity.Rol;
import com.example.backend.entity.User;
import com.example.backend.repository.ProfesionalRepository;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UserRepository;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProfesionalRepository profesionalRepository;
    private final RolRepository rolRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       ProfesionalRepository profesionalRepository,
                       RolRepository rolRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.profesionalRepository = profesionalRepository;
        this.rolRepository = rolRepository;
    }

    // ==================== Autenticación ====================
    public void authenticate(String correo, String contrasena) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correo, contrasena)
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    // ==================== Registro de usuarios ====================
    public User registerAndReturnUser(String correo, String contrasena) {
        if (userRepository.findByCorreo(correo).isPresent()) {
            return null; // Usuario ya existe
        }
        User nuevoUsuario = new User();
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setContrasena(passwordEncoder.encode(contrasena));
        return userRepository.save(nuevoUsuario);
    }

    public User findByCorreo(String correo) {
        return userRepository.findByCorreo(correo).orElse(null);
    }

    public List<Rol> getRoles(Long usuarioId) {
        User user = userRepository.findById(usuarioId).orElse(null);
        return user != null ? List.copyOf(user.getRoles()) : List.of();
    }

    // ==================== Profesionales ====================
    public Profesional saveProfesional(Profesional profesional) {
        return profesionalRepository.save(profesional);
    }

    public Profesional getProfesionalByUsuarioId(Long usuarioId) {
        return profesionalRepository.findByUsuarioId(usuarioId).orElse(null);
    }

}
