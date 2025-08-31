package com.example.backend.service;

import com.example.backend.entity.PasswordResetToken;
import com.example.backend.entity.Usuario;
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

    private final UsuarioRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UsuarioRepository userRepository,
                                PasswordResetTokenRepository tokenRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String sendPasswordResetToken(String correo) {
        Optional<Usuario> userOptional = userRepository.findByCorreo(correo);

        if (userOptional.isEmpty()) {
            logger.warn("❌ Usuario no encontrado: {}", correo);
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario user = userOptional.get();

        tokenRepository.deleteByUsuario(user);
        logger.info("🧹 Tokens anteriores eliminados para: {}", correo);

        String tokenString = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken(tokenString, user);
        tokenRepository.save(token);

        logger.info("🔐 Nuevo token generado para {}: {}", correo, tokenString);

        return "Token generado correctamente (modo desarrollo): " + tokenString;
    }

    @Transactional
    public String resetPassword(String token, String newPassword) {
        logger.info("🔁 Procesando reseteo de contraseña para token: {}", token);

        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);

        if (tokenOptional.isEmpty()) {
            logger.warn("⛔ Token inválido: {}", token);
            return "Token inválido o no encontrado.";
        }

        PasswordResetToken resetToken = tokenOptional.get();

        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            logger.warn("⏰ Token expirado: {}", token);
            tokenRepository.delete(resetToken);
            return "Token expirado.";
        }

        Usuario user = resetToken.getUsuario();

        user.setContrasena(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("✅ Contraseña actualizada para usuario: {}", user.getCorreo());

        tokenRepository.delete(resetToken);

        return "Contraseña actualizada correctamente.";
    }
}
