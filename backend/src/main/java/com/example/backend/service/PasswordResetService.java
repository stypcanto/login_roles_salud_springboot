package com.example.backend.service;

import com.example.backend.entity.PasswordResetToken;
import com.example.backend.model.Usuario;
import com.example.backend.repository.PasswordResetTokenRepository;
import com.example.backend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

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
        logger.info("📩 Solicitud de recuperación de contraseña para: {}", correo);

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> {
                    logger.warn("❌ Usuario no encontrado: {}", correo);
                    return new RuntimeException("Usuario no encontrado");
                });

        // Elimina tokens anteriores
        tokenRepository.deleteByUsuario(usuario);
        logger.info("🧹 Tokens anteriores eliminados para el usuario: {}", correo);

        // Crea nuevo token
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUsuario(usuario);
        token.setExpiration(LocalDateTime.now().plusHours(1));
        tokenRepository.save(token);

        logger.info("🔐 Nuevo token generado para {}: {}", correo, token.getToken());

        // En producción se enviaría por correo
        return "Token generado correctamente (modo desarrollo): " + token.getToken();
    }

    @Transactional
    public String resetPassword(String token, String newPassword) {
        logger.info("🔁 Intentando resetear contraseña con token: {}", token);

        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);

        if (tokenOptional.isEmpty()) {
            logger.warn("⛔ Token inválido o expirado: {}", token);
            return "Token inválido o expirado.";
        }

        PasswordResetToken resetToken = tokenOptional.get();

        // Verifica expiración
        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            logger.warn("⏰ Token expirado: {}", token);
            return "Token expirado.";
        }

        Usuario usuario = resetToken.getUsuario();

        // Busca usuario de nuevo por correo
        Usuario existente = usuarioRepository.findByCorreo(usuario.getCorreo())
                .orElseThrow(() -> {
                    logger.error("❌ Usuario no encontrado al usar token");
                    return new RuntimeException("Usuario no encontrado");
                });

        existente.setContrasena(passwordEncoder.encode(newPassword));
        usuarioRepository.save(existente);

        tokenRepository.delete(resetToken); // Limpia token usado

        logger.info("✅ Contraseña actualizada para usuario: {}", existente.getCorreo());

        return "Contraseña actualizada correctamente.";
    }
}
