package com.example.backend.controller;

import com.example.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public boolean login(@RequestBody Map<String, String> credentials) {
        String correo = credentials.get("correo");
        String contraseña = credentials.get("contraseña");

        // ⚠ Solo para desarrollo (NO en producción)
        System.out.println("📨 Correo recibido: " + correo);
        System.out.println("🔐 Contraseña recibida: " + contraseña);

        return authService.login(correo, contraseña);
    }
}
