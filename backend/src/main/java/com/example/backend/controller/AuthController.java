package com.example.backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import jakarta.validation.constraints.Size;

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

    // ================= PING =================
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        logger.info("🎏 Ping recibido en /api/auth/ping");
        return ResponseEntity.ok("Pong");
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena())
            );

            Usuario usuario = authService.findByCorreo(request.correo());

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }

            if (!usuario.isActivo()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Usuario pendiente de aprobación por el área TI");
            }

            String token = jwtUtil.generateToken(usuario.getCorreo());

            List<String> roles = usuario.getRoles() != null
                    ? usuario.getRoles().stream().map(Rol::getNombre).toList()
                    : List.of();

            List<String> permisos = List.of(); // Por ahora vacío

            Profesional profesional = authService.getProfesionalByUsuarioId(usuario.getId());
            if (profesional == null) profesional = new Profesional(); // Opcional

            return ResponseEntity.ok(new JwtLoginResponse(
                    token,
                    roles,
                    permisos,
                    profesional));

        } catch (AuthenticationException e) {
            logger.warn("❌ Fallo de login para {}: credenciales inválidas", request.correo());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            logger.error("❌ Error inesperado en login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor");
        }
    }

    // ================= REGISTRO =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            Set<String> rolesSet = request.roles() != null ? new HashSet<>(request.roles()) : new HashSet<>();

            // ⚡ Corrección: ahora pasamos los 7 parámetros requeridos
            Usuario usuario = authService.registerAndReturnUser(
                    request.correo(),
                    request.contrasena(),
                    request.nombres(),
                    request.apellidos(),
                    request.tipoDocumento(),
                    request.documento(),
                    rolesSet
            );

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

            logger.info("✅ Usuario {} registrado correctamente", request.correo());
            return ResponseEntity.ok("Usuario registrado correctamente. Pendiente de aprobación por el área TI.");

        } catch (Exception e) {
            logger.error("❌ Error en registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor");
        }
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
            @NotBlank @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String contrasena,
            @NotBlank String nombres,
            @NotBlank String apellidos,
            @NotBlank String tipoDocumento,
            @NotBlank @Size(min = 6, max = 20) String documento,
            String telefono,
            List<String> roles // ahora permitimos múltiples roles
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
            @NotBlank @Size(min = 6) String newPassword
    ) {}

    // ================= RESPUESTA =================
    public record JwtLoginResponse(
            String token,
            List<String> roles,
            List<String> permisos,
            Profesional profesional
    ) {}
}
