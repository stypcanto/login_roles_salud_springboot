package com.example.backend.controller;

import com.example.backend.service.AuthService;
import com.example.backend.service.PasswordResetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        logger.info("\uD83C\uDF8F Ping recibido en /api/auth/ping");
        return ResponseEntity.ok("Pong");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        logger.info("\uD83D\uDFE2 Intento de login: {}", request.correo());

        boolean isValid = authService.login(request.correo(), request.contrasena());

        if (isValid) {
            logger.info("\u2705 Login exitoso para: {}", request.correo());
            return ResponseEntity.ok(new LoginResponse(true, "Inicio de sesión exitoso"));
        } else {
            logger.warn("\u274C Login fallido para: {}", request.correo());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Credenciales inválidas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        logger.info("\uD83D\uDCDD Intento de registro: {}", request.correo());

        boolean registrado = authService.register(request.correo(), request.contrasena());

        if (registrado) {
            logger.info("\u2705 Registro exitoso para: {}", request.correo());
            return ResponseEntity.ok("Usuario registrado correctamente.");
        } else {
            logger.warn("\u26A0\uFE0F Registro fallido, correo ya existe: {}", request.correo());
            return ResponseEntity.badRequest().body("El usuario ya existe.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        logger.info("\uD83D\uDD14 Recuperación de contraseña para: {}", request.correo());
        String response = passwordResetService.sendPasswordResetToken(request.correo());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        logger.info("\uD83D\uDD01 Reinicio de contraseña con token: {}", request.token());
        String response = passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok(response);
    }

    // DTOs con validaciones
    public record RegisterRequest(
            @NotBlank @Email String correo,
            @NotBlank String contrasena) {}

    public record LoginRequest(
            @NotBlank @Email String correo,
            @NotBlank String contrasena) {}

    public record ForgotPasswordRequest(
            @NotBlank @Email String correo) {}

    public record ResetPasswordRequest(
            @NotBlank String token,
            @NotBlank String newPassword) {}

    public record LoginResponse(
            boolean success,
            String message) {}
}
