package com.example.backend.service;

import com.example.backend.entity.PasswordResetToken;
import com.example.backend.model.Usuario;
import com.example.backend.repository.PasswordResetTokenRepository;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
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
        System.out.println("🟡 Buscando usuario con correo: " + correo);

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado: " + correo));

        tokenRepository.deleteByUsuario(usuario);
        System.out.println("✅ Tokens anteriores eliminados para el usuario");

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUsuario(usuario);
        token.setExpiration(LocalDateTime.now().plusHours(1));
        tokenRepository.save(token);

        System.out.println("🔐 Nuevo token generado: " + token.getToken());

        return "Token generado (en producción se enviaría por email): " + token.getToken();
    }

    @Transactional
   public String resetPassword(String token, String newPassword) {
    Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);

    if (tokenOptional.isEmpty()) {
        return "Token inválido o expirado.";
    }

    PasswordResetToken resetToken = tokenOptional.get();
    Usuario usuario = resetToken.getUsuario();

    // Asegúrate que es un usuario existente en la BD (si no se hizo correctamente)
    Usuario existente = usuarioRepository.findByCorreo(usuario.getCorreo())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    existente.setContrasena(passwordEncoder.encode(newPassword));
    usuarioRepository.save(existente);

    tokenRepository.delete(resetToken); // Limpia token usado

    return "Contraseña actualizada correctamente.";
}
}