package com.example.backend.security;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Buscar el usuario por correo
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        System.out.println("🔍 Usuario encontrado: " + user.getCorreo());
        System.out.println("🔍 Hash en BD: " + user.getContrasena());
        System.out.println("🔍 Roles: " + user.getRoles().stream().map(r -> r.getNombre()).toList());

        // Convertir roles a GrantedAuthority
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());

        // Devolver el usuario autenticable
        return org.springframework.security.core.userdetails.User.withUsername(user.getCorreo())
                .password(user.getContrasena())
                .authorities(authorities) // 👈 ahora sí usa los roles dinámicamente
                .accountLocked(!user.isActivo())
                .build();
    }
}
