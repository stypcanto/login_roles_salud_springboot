package com.example.backend.controller;

import com.example.backend.service.AuthService;
import com.example.backend.service.PasswordResetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("🏓 Ping recibido en /api/auth/test");
        return ResponseEntity.ok("API funcionando");
    }

    @GetMapping("/ping")
public ResponseEntity<String> ping() {
    logger.info("🏓 Ping recibido en /api/auth/ping");
    return ResponseEntity.ok("Pong");
}
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        logger.info("🟢 Intento de inicio de sesión para: {}", request.correo());

        boolean isValid = authService.login(request.correo(), request.contrasena());

        if (isValid) {
            logger.info("✅ Usuario autenticado correctamente: {}", request.correo());
            return ResponseEntity.ok(new LoginResponse(true, "Inicio de sesión exitoso"));
        } else {
            logger.warn("❌ Fallo de autenticación para: {}", request.correo());
            return ResponseEntity.status(401).body(new LoginResponse(false, "Credenciales inválidas"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        logger.info("🔔 Solicitud de recuperación de contraseña para: {}", request.correo());
        String response = passwordResetService.sendPasswordResetToken(request.correo());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        logger.info("🔁 Reseteo de contraseña con token: {}", request.token());
        String response = passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        logger.info("📝 Intento de registro para: {}", request.correo());

        boolean registrado = authService.register(request.correo(), request.contrasena());

        if (registrado) {
            logger.info("✅ Usuario registrado exitosamente: {}", request.correo());
            return ResponseEntity.ok("Usuario registrado correctamente.");
        } else {
            logger.warn("⚠️ Registro fallido: ya existe usuario con correo: {}", request.correo());
            return ResponseEntity.badRequest().body("El usuario ya existe.");
        }
    }

    public record RegisterRequest(
            @NotBlank @Email String correo,
            @NotBlank String contrasena) {
    }

    // Records para DTOs con validaciones

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
}
