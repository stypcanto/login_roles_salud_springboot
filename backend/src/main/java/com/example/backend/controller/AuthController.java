package com.example.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import com.example.backend.entity.Profesional;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import com.example.backend.security.JwtUtil;
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
        logger.info("🎏 Ping recibido en /api/auth/ping");
        return ResponseEntity.ok("Pong");
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena())
            );

            Usuario usuario = authService.findByCorreo(request.correo());

            if (!usuario.isActivo()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Usuario pendiente de aprobación por el área TI");
            }

            String token = jwtUtil.generateToken(usuario.getCorreo());

            List<Rol> roles = authService.getRoles(usuario.getId());
            List<String> permisos = roles.stream()
                    .flatMap(r -> r.getPermisos().stream())
                    .map(p -> p.getNombre())
                    .distinct()
                    .collect(Collectors.toList());

            Profesional profesional = authService.getProfesionalByUsuarioId(usuario.getId());

            return ResponseEntity.ok(new JwtLoginResponse(token,
                    roles.stream().map(Rol::getNombre).toList(),
                    permisos,
                    profesional));

        } catch (AuthenticationException e) {
            logger.warn("❌ Fallo de login: credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            logger.error("❌ Error inesperado en login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor");
        }
    }

    // ================= REGISTRO =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Usuario usuario = authService.registerAndReturnUser(request.correo(), request.contrasena());

        if (usuario == null) {
            return ResponseEntity.badRequest().body("El usuario ya existe.");
        }

        Profesional profesional = new Profesional();
        profesional.setUsuario(usuario);
        profesional.setNombres(request.nombres());
        profesional.setApellidos(request.apellidos());
        profesional.setDocumento(request.documento());
        profesional.setTipoDocumento(request.tipoDocumento());
        profesional.setTelefono(request.telefono());

        authService.saveProfesional(profesional);

        authService.assignRole(usuario.getId(), "COORDINADOR_MEDICO");

        return ResponseEntity.ok("Usuario registrado correctamente. Pendiente de aprobación por el área TI.");
    }

    // ================= RECUPERACIÓN DE CONTRASEÑA =================
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

    // ================= DTOs =================
    public record RegisterRequest(
            @NotBlank @Email String correo,
            @NotBlank String contrasena,
            @NotBlank String nombres,
            @NotBlank String apellidos,
            @NotBlank String tipoDocumento,
            @NotBlank String documento,
            String telefono
    ) {}

    public record LoginRequest(
            @NotBlank @Email String correo,
            @NotBlank String contrasena
    ) {}

    public record ForgotPasswordRequest(
            @NotBlank @Email String correo
    ) {}

    public record ResetPasswordRequest(
            @NotBlank String token,
            @NotBlank String newPassword
    ) {}

    // ================= RESPUESTA =================
    public record JwtLoginResponse(
            String token,
            List<String> roles,
            List<String> permisos,
            Profesional profesional
    ) {}
}
