package com.example.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



public String authenticate(String correo, String contrasena) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(correo, contrasena));
        // Si no lanza excepción, generar token
        return jwtUtil.generateToken(correo);
    } catch (AuthenticationException e) {
        throw new RuntimeException("Credenciales inválidas");
    }
}


    public boolean register(String correo, String contrasena) {
        if (userRepository.findByCorreo(correo).isPresent()) {
            return false; // Usuario ya existe
        }
        User nuevoUsuario = new User();
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setContrasena(passwordEncoder.encode(contrasena));
        userRepository.save(nuevoUsuario);
        return true;
    }
}
