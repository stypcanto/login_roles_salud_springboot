package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("authServiceService")
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(String correo, String contrasena) {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(contrasena, user.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generateToken(user.getCorreo());
    }

    // 👇 Método que te faltaba
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
