package com.example.backend.service;

import com.example.backend.entity.Profesional;
import com.example.backend.entity.Rol;
import com.example.backend.entity.Usuario;
import com.example.backend.repository.ProfesionalRepository;
import com.example.backend.repository.RolRepository;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ProfesionalRepository profesionalRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       RolRepository rolRepository,
                       ProfesionalRepository profesionalRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.profesionalRepository = profesionalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registrar usuario y devolverlo
     */
    public Usuario registerAndReturnUser(String correo,
                                         String contrasena,
                                         String nombres,
                                         String apellidos,
                                         String tipoDocumento,
                                         String documento,
                                         Set<String> nombresRoles) {

        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            return null; // usuario ya existe
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setTipoDocumento(tipoDocumento);
        usuario.setDocumento(documento);
        usuario.setActivo(true);

        Set<Rol> roles = getOrCreateRoles(nombresRoles);
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }

    /**
     * Crear o recuperar roles existentes
     */
    public Set<Rol> getOrCreateRoles(Set<String> nombresRoles) {
        Set<Rol> roles = new HashSet<>();
        if (nombresRoles != null) {
            for (String nombreRol : nombresRoles) {
                Rol rol = rolRepository.findByNombre(nombreRol)
                        .orElseGet(() -> {
                            Rol nuevoRol = new Rol();
                            nuevoRol.setNombre(nombreRol);
                            return rolRepository.save(nuevoRol);
                        });
                roles.add(rol);
            }
        }
        return roles;
    }

    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    public Profesional getProfesionalByUsuarioId(Long usuarioId) {
        return profesionalRepository.findByUsuario_Id(usuarioId).orElse(null);
    }

    public void saveProfesional(Profesional profesional) {
        profesionalRepository.save(profesional);
    }

    public void saveUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }
}
