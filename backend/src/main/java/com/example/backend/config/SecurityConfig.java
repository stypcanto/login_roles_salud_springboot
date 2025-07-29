package com.example.backend.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @PostConstruct
    public void init() {
        logger.info("🔐 Configuración de seguridad cargada");
    }

    // Bean para codificar contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("🔑 Bean PasswordEncoder (BCrypt) inicializado");
        return new BCryptPasswordEncoder();
    }

    // Configuración del filtro de seguridad principal
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("🛡️ Configurando SecurityFilterChain...");

        http
                // Deshabilita CSRF (útil en APIs sin sesiones)
                .csrf(csrf -> {
                    logger.info("❌ CSRF deshabilitado");
                    csrf.disable();
                })

                // Activa CORS utilizando el bean corsConfigurationSource()
                .cors(cors -> {
                    logger.info("🌐 CORS habilitado");
                    cors.configurationSource(corsConfigurationSource());
                })

                // Desactiva formulario login
                .formLogin(form -> {
                    logger.info("🚫 formLogin deshabilitado");
                    form.disable();
                })

                // Desactiva HTTP Basic
                .httpBasic(basic -> {
                    logger.info("🚫 httpBasic deshabilitado");
                    basic.disable();
                })

                // Rutas públicas y protegidas
                .authorizeHttpRequests(auth -> {
                    logger.info("✅ Configurando rutas públicas y protegidas");

                    auth
                            .requestMatchers(
                                    "/api/auth/login",
                                    "/api/auth/register",
                                    "/api/auth/login",
                                    "/api/auth/reset",
                                    "/ping",
                                    "/error")
                            .permitAll() // acceso libre
                            .anyRequest().authenticated(); // el resto requiere autenticación
                });

        logger.info("✅ SecurityFilterChain configurado correctamente");
        return http.build();
    }

    // Bean CORS global: solo necesitas uno
    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // Permitir cookies (si se usan)
        config.setAllowedOrigins(List.of(
                "http://localhost",
                "http://localhost:80",
                "http://localhost:5173"));// ⚠️ En producción: usa tu dominio (ej. "https://miapp.com")
        config.setAllowedHeaders(List.of("*")); // Permitir todos los headers
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos HTTP permitidos

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // ❌ Este segundo filtro ya no es necesario si usamos corsConfigurationSource()
    // arriba
    // Si lo dejas, puede causar conflictos o doble configuración
    // Elimínalo para mantener un solo enfoque
}
