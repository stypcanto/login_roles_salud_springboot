package com.example.backend.service;

import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean login(String correo, String contraseña) {
        return usuarioRepository.findByCorreo(correo)
                .map(user -> user.getContraseña().equals(contraseña)) // En producción: usar hashing
                .orElse(false);
    }
}
