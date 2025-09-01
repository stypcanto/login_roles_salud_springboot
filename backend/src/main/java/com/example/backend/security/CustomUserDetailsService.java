package com.example.backend.security;

import com.example.backend.entity.Usuario;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        String[] rolesArray = usuario.getRoles() != null
                ? usuario.getRoles().stream().map(r -> r.getNombre()).toArray(String[]::new)
                : new String[]{"USER"};

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasena())
                .roles(rolesArray)
                .disabled(!usuario.isActivo())
                .build();
    }
}
