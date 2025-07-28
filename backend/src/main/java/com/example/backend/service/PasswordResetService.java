package com.example.backend.service;

import com.example.backend.entity.PasswordResetToken;
import com.example.backend.model.Usuario;
import com.example.backend.repository.PasswordResetTokenRepository;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UsuarioRepository usuarioRepository,
                              PasswordResetTokenRepository tokenRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String sendPasswordResetToken(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con este correo: " + correo));

        // Eliminar tokens existentes para este usuario
        tokenRepository.deleteByUsuario(usuario);

        // Crear y guardar nuevo token
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUsuario(usuario);
        token.setExpiration(LocalDateTime.now().plusHours(1)); // 1 hora de validez
        tokenRepository.save(token);

        // En producción: enviar email con el enlace de reset
        return "Token generado (en producción se enviaría por email): " + token.getToken();
    }

    @Transactional
    public String resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o expirado"));

        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setcontrasena(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);

        // Eliminar el token usado
        tokenRepository.delete(resetToken);

        return "Contraseña actualizada exitosamente";
    }
}