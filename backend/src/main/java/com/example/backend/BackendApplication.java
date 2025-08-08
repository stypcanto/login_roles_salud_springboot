package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Esta es la clase principal de la aplicación Spring Boot.
 * 
 * - La anotación @SpringBootApplication:
 *   Combina 3 anotaciones importantes:
 *     1. @Configuration → Indica que esta clase puede contener configuración de Spring.
 *     2. @EnableAutoConfiguration → Hace que Spring Boot configure automáticamente el proyecto
 *        en base a las dependencias (por ejemplo, si tienes Spring Web, configura un servidor Tomcat).
 *     3. @ComponentScan → Hace que Spring busque automáticamente todos los componentes, controladores,
 *        servicios, etc., dentro del paquete "com.example.backend" y sus subpaquetes.
 * 
 * - Método main():
 *   Es el punto de entrada de la aplicación.
 *   Llama a SpringApplication.run() que:
 *     1. Arranca el contexto de Spring.
 *     2. Realiza el escaneo de componentes.
 *     3. Levanta el servidor web embebido (Tomcat por defecto).
 *     4. Mantiene la aplicación en ejecución escuchando peticiones HTTP.
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        // Inicia la aplicación Spring Boot.
        // 'BackendApplication.class' le dice a Spring que esta es la clase principal.
        SpringApplication.run(BackendApplication.class, args);
    }
}
