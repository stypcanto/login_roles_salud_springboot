package com.example.backend.controller;

import com.example.backend.dto.ProfesionalCreateDTO;
import com.example.backend.dto.ProfesionalDTO;
import com.example.backend.entity.Profesional;
import com.example.backend.entity.Rol;
import com.example.backend.entity.User;
import com.example.backend.repository.ProfesionalRepository;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para profesionales.
 *
 * Buenas prácticas implementadas:
 * - Separación clara: tabla Profesional independiente de Usuario.
 * - Validación de usuario existente y activo antes de asignarlo.
 * - Asignación automática del rol "Profesional" si el usuario no lo tiene.
 * - Uso de DTO combinado (ProfesionalDTO) para simplificar frontend.
 * - Método auxiliar para centralizar la lógica de asignación usuario-rol.
 */
@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalController {

    private final ProfesionalRepository profesionalRepository;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public ProfesionalController(ProfesionalRepository profesionalRepository,
                                 UserRepository userRepository,
                                 RolRepository rolRepository) {
        this.profesionalRepository = profesionalRepository;
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    // ===================== GET TODOS =====================
    @GetMapping
    public ResponseEntity<List<ProfesionalDTO>> getAllProfesionales() {
        // Mapear todas las entidades Profesional a DTOs
        List<ProfesionalDTO> profesionales = profesionalRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(profesionales);
    }

    // ===================== GET POR ID =====================
    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> getProfesionalById(@PathVariable Long id) {
        return profesionalRepository.findById(id)
                .map(this::mapToDTO) // Reutilizamos método de mapeo
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===================== POST =====================
    @PostMapping
    @Transactional // Asegura que operaciones sobre usuario y profesional sean atómicas
    public ResponseEntity<ProfesionalDTO> createProfesional(
            @Valid @RequestBody ProfesionalCreateDTO dto) {

        Profesional profesional = new Profesional();
        profesional.setNombres(dto.nombres());
        profesional.setApellidos(dto.apellidos());
        profesional.setTipoDocumento(dto.tipoDocumento());
        profesional.setNumeroDocumento(dto.numeroDocumento());
        profesional.setRne(dto.rne());
        profesional.setColegiatura(dto.colegiatura());
        profesional.setEspecialidad(dto.especialidad());
        profesional.setTelefono(dto.telefono());
        profesional.setFechaNacimiento(dto.fechaNacimiento());

        // Asignación de usuario y rol “Profesional”
        asignarUsuarioYRol(profesional, dto.correoUsuario());

        Profesional saved = profesionalRepository.save(profesional);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(saved));
    }

    // ===================== PUT =====================
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ProfesionalDTO> updateProfesional(
            @PathVariable Long id,
            @Valid @RequestBody ProfesionalCreateDTO dto) {

        return profesionalRepository.findById(id)
                .map(profesional -> {
                    profesional.setNombres(dto.nombres());
                    profesional.setApellidos(dto.apellidos());
                    profesional.setTipoDocumento(dto.tipoDocumento());
                    profesional.setNumeroDocumento(dto.numeroDocumento());
                    profesional.setRne(dto.rne());
                    profesional.setColegiatura(dto.colegiatura());
                    profesional.setEspecialidad(dto.especialidad());
                    profesional.setTelefono(dto.telefono());
                    profesional.setFechaNacimiento(dto.fechaNacimiento());

                    // Asignación de usuario y rol “Profesional”
                    asignarUsuarioYRol(profesional, dto.correoUsuario());

                    Profesional updated = profesionalRepository.save(profesional);
                    return ResponseEntity.ok(mapToDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ===================== DELETE =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesional(@PathVariable Long id) {
        if (profesionalRepository.existsById(id)) {
            profesionalRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ===================== MÉTODOS AUXILIARES =====================

    /**
     * Mapea Profesional → ProfesionalDTO combinando datos de usuario y roles.
     */
    private ProfesionalDTO mapToDTO(Profesional p) {
        return new ProfesionalDTO(
                p.getId(),
                p.getNombres(),
                p.getApellidos(),
                p.getTipoDocumento(),
                p.getNumeroDocumento(),
                p.getRne(),
                p.getColegiatura(),
                p.getEspecialidad(),
                p.getTelefono(),
                p.getFechaNacimiento(),
                p.getUsuario() != null ? p.getUsuario().getCorreo() : null,
                p.getUsuario() != null ? p.getUsuario().isActivo() : false,
                p.getUsuario() != null
                        ? p.getUsuario().getRoles().stream()
                        .map(r -> r.getNombre()).toList()
                        : List.of()
        );
    }

    /**
     * Asigna un usuario existente a un profesional y se asegura de que tenga el rol "Profesional".
     * @param profesional Profesional a asignar.
     * @param correoUsuario Correo del usuario existente.
     */
    private void asignarUsuarioYRol(Profesional profesional, String correoUsuario) {
        if (correoUsuario == null) return;

        User user = userRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un usuario con el correo: " + correoUsuario));

        if (!user.isActivo()) {
            throw new IllegalStateException("El usuario no está activo");
        }

        // Si no tiene el rol "Profesional", asignarlo automáticamente
        boolean tieneRolProfesional = user.getRoles().stream()
                .anyMatch(r -> r.getNombre().equals("Profesional"));

        if (!tieneRolProfesional) {
            Rol rolProfesional = rolRepository.findByNombre("Profesional")
                    .orElseThrow(() -> new IllegalStateException("No existe el rol Profesional"));
            user.getRoles().add(rolProfesional);
            userRepository.save(user); // Guardamos cambios en usuario
        }

        profesional.setUsuario(user); // Finalmente vinculamos usuario al profesional
    }
}
