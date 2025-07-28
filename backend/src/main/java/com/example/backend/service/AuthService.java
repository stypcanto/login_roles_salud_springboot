package com.example.backend.service;

import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Autentica a un usuario validando su correo y contraseña.
     * 
     * @param correo     Correo electrónico del usuario.
     * @param contrasena Contraseña sin cifrar ingresada por el usuario.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean login(String correo, String contrasena) {
        logger.info("🟡 Intentando autenticar usuario con correo: {}", correo);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            logger.warn("⚠️ Usuario no encontrado con correo: {}", correo);
            return false;
        }

        Usuario usuario = usuarioOpt.get();

        boolean contrasenaValida = passwordEncoder.matches(contrasena, usuario.getContrasena());

        if (contrasenaValida) {
            logger.info("✅ Autenticación exitosa para: {}", correo);
            return true;
        } else {
            logger.warn("❌ Contraseña incorrecta para: {}", correo);
            return false;
        }
    }

    /**
     * Registra un nuevo usuario si el correo no está ya registrado.
     * 
     * @param correo     Correo del nuevo usuario.
     * @param contrasena Contraseña sin cifrar.
     * @return true si el usuario fue registrado, false si ya existía.
     */
    public boolean register(String correo, String contrasena) {
        logger.info("📝 Intentando registrar nuevo usuario: {}", correo);

        if (usuarioRepository.existsByCorreo(correo)) {
            logger.warn("🚫 Ya existe un usuario registrado con el correo: {}", correo);
            return false;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setContrasena(passwordEncoder.encode(contrasena));

        usuarioRepository.save(nuevoUsuario);
        logger.info("✅ Usuario registrado exitosamente: {}", correo);
        return true;
    }
}
