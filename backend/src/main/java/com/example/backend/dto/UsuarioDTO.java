package com.example.backend.dto;

public class UsuarioDTO {

    private Long id;
    private String correo;
    private boolean activo;
    private String rol;

    public UsuarioDTO(Long id, String correo, boolean activo, String rol) {
        this.id = id;
        this.correo = correo;
        this.activo = activo;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
