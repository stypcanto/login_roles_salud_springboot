package com.example.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.security.JwtUtil;
import com.example.backend.service.AuthService;
import com.example.backend.service.PasswordResetService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService,
            PasswordResetService passwordResetService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        logger.info("🎏 Ping recibido en /auth/ping");
        return ResponseEntity.ok("Pong");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        logger.info("Intento de login con correo: {}", request.correo());
        try {
            // Aquí autenticas con Spring Security:
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena()));

            // Si no lanza excepción, generas el token:
            String token = jwtUtil.generateToken(request.correo());
            logger.info("✅ Login exitoso para: {}", request.correo());
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (AuthenticationException e) {
            logger.warn("❌ Fallo en login para {}: Credenciales inválidas", request.correo());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            logger.error("❌ Error inesperado en login para {}: {}", request.correo(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("📝 Intento de registro: {}", request.correo());

        boolean registrado = authService.register(request.correo(), request.contrasena());

        if (registrado) {
            logger.info("✅ Registro exitoso para: {}", request.correo());
            return ResponseEntity.ok("Usuario registrado correctamente.");
        } else {
            logger.warn("⚠️ Registro fallido, correo ya existe: {}", request.correo());
            return ResponseEntity.badRequest().body("El usuario ya existe.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        logger.info("🔔 Recuperación de contraseña para: {}", request.correo());
        String response = passwordResetService.sendPasswordResetToken(request.correo());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        logger.info("🔁 Reinicio de contraseña con token: {}", request.token());
        String response = passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok(response);
    }

    // ✅ DTOs con validaciones
    public record RegisterRequest(
            @NotBlank @Email String correo,
            @NotBlank String contrasena) {
    }

    public record LoginRequest(
            @NotBlank @Email String correo,
            @NotBlank String contrasena) {
    }

    public record ForgotPasswordRequest(
            @NotBlank @Email String correo) {
    }

    public record ResetPasswordRequest(
            @NotBlank String token,
            @NotBlank String newPassword) {
    }

    public record LoginResponse(
            boolean success,
            String message) {
    }

    public record JwtResponse(String token) {
    }
}
