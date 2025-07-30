package com.example.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.service.AuthService;
import com.example.backend.service.PasswordResetService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        logger.info("🎏 Ping recibido en /api/auth/ping");
        return ResponseEntity.ok("Pong");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticate(request.correo(), request.contrasena());
            logger.info("✅ Login exitoso para: {}", request.correo());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException e) {
            logger.warn("❌ Fallo en login para {}: {}", request.correo(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request)  {
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

    public record JwtResponse(String token) {}
}
