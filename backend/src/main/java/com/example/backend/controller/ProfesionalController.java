package com.example.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.entity.Profesional;
import com.example.backend.entity.Usuario;
import com.example.backend.service.AuthService;
import com.example.backend.service.ProfesionalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalController {

    private static final Logger logger = LoggerFactory.getLogger(ProfesionalController.class);

    private final ProfesionalService profesionalService;
    private final AuthService authService;

    public ProfesionalController(ProfesionalService profesionalService, AuthService authService) {
        this.profesionalService = profesionalService;
        this.authService = authService;
    }

    // ================= LISTAR TODOS LOS PROFESIONALES =================
    @GetMapping
    public ResponseEntity<List<Profesional>> getAllProfesionales() {
        List<Profesional> profesionales = profesionalService.getAllProfesionales();
        return ResponseEntity.ok(profesionales);
    }

    // ================= OBTENER PROFESIONAL POR ID =================
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfesionalById(@PathVariable Long id) {
        Profesional profesional = profesionalService.getProfesionalById(id);
        if (profesional == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesional no encontrado");
        }
        return ResponseEntity.ok(profesional);
    }

    // ================= OBTENER PROFESIONAL POR USUARIO =================
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getProfesionalByUsuario(@PathVariable Long usuarioId) {
        Profesional profesional = profesionalService.getProfesionalByUsuarioId(usuarioId);
        if (profesional == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesional no encontrado para este usuario");
        }
        return ResponseEntity.ok(profesional);
    }

    // ================= CREAR PROFESIONAL =================
    @PostMapping
    public ResponseEntity<?> createProfesional(@Valid @RequestBody Profesional profesional) {
        try {
            // Validar si el usuario ya tiene profesional asociado
            Usuario usuario = profesional.getUsuario();
            if (usuario != null && profesionalService.getProfesionalByUsuarioId(usuario.getId()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El usuario ya tiene un profesional asociado");
            }

            Profesional saved = profesionalService.saveProfesional(profesional);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("Error al crear profesional: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear profesional");
        }
    }

    // ================= ACTUALIZAR PROFESIONAL =================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfesional(@PathVariable Long id, @Valid @RequestBody Profesional profesional) {
        Profesional existing = profesionalService.getProfesionalById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesional no encontrado");
        }

        // Actualizar campos
        existing.setNombres(profesional.getNombres());
        existing.setApellidos(profesional.getApellidos());
        existing.setDocumento(profesional.getDocumento());
        existing.setTipoDocumento(profesional.getTipoDocumento());
        existing.setTelefono(profesional.getTelefono());
        existing.setEspecialidad(profesional.getEspecialidad());
        existing.setUsuario(profesional.getUsuario());

        Profesional updated = profesionalService.saveProfesional(existing);
        return ResponseEntity.ok(updated);
    }

    // ================= ELIMINAR PROFESIONAL =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfesional(@PathVariable Long id) {
        Profesional existing = profesionalService.getProfesionalById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesional no encontrado");
        }

        profesionalService.deleteProfesional(id);
        return ResponseEntity.ok("Profesional eliminado correctamente");
    }

    // ================= LISTAR PROFESIONALES POR ESPECIALIDAD =================
    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<List<Profesional>> getProfesionalesByEspecialidad(@PathVariable Long especialidadId) {
        List<Profesional> profesionales = profesionalService.getProfesionalesByEspecialidad(especialidadId);
        return ResponseEntity.ok(profesionales);
    }
}
