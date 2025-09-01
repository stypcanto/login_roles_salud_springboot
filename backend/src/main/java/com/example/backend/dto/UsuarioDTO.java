package com.example.backend.dto;

import java.util.List;

public class UsuarioDTO {

    private Long id;
    private String correo;
    private boolean activo;
    private List<String> roles; // <-- cambiamos a lista de roles

    // ✅ Constructor vacío (necesario para frameworks como Jackson)
    public UsuarioDTO() {
    }

    // ✅ Constructor completo
    public UsuarioDTO(Long id, String correo, boolean activo, List<String> roles) {
        this.id = id;
        this.correo = correo;
        this.activo = activo;
        this.roles = roles;
    }

    // ✅ Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // ✅ Método toString para depuración/logs
    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", correo='" + correo + '\'' +
                ", activo=" + activo +
                ", roles=" + roles +
                '}';
    }
}
