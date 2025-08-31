-- =========================================================
--  Base de Datos: Sistema de Usuarios y Profesionales de Salud
--  Propósito: Manejar usuarios, roles, permisos, profesionales médicos,
--             especialidades y recuperación de contraseña.
-- =========================================================

-- =========================
-- Tabla: usuarios
-- =========================
-- Esta tabla almacena información básica de los usuarios del sistema.
-- Cada usuario tiene correo único y contraseña (hashed), estado activo/inactivo,
-- nombres y apellidos, tipo y número de documento, y timestamps de creación y actualización.
CREATE TABLE usuarios (
                          id SERIAL PRIMARY KEY,                      -- ID único autoincremental
                          correo VARCHAR(255) UNIQUE NOT NULL,       -- Correo electrónico único
                          contrasena VARCHAR(255) NOT NULL,          -- Contraseña encriptada (BCrypt)
                          activo BOOLEAN DEFAULT TRUE,               -- Estado del usuario
                          nombres VARCHAR(100) NOT NULL,             -- Nombres del usuario
                          apellidos VARCHAR(100) NOT NULL,           -- Apellidos del usuario
                          tipo_documento VARCHAR(20),                -- Tipo de documento (DNI, Pasaporte, etc.)
                          documento VARCHAR(20),                      -- Número de documento
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Tabla: roles
-- =========================
-- Define los roles que los usuarios pueden tener en el sistema.
-- Ejemplo: ADMIN, COORDINADOR, MEDICO.
CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       nombre VARCHAR(50) UNIQUE NOT NULL,        -- Nombre del rol
                       descripcion TEXT                           -- Descripción opcional del rol
);

-- =========================
-- Tabla: usuario_roles
-- =========================
-- Relación N:M entre usuarios y roles.
-- Un usuario puede tener múltiples roles y un rol puede asignarse a múltiples usuarios.
CREATE TABLE usuario_roles (
                               usuario_id INT REFERENCES usuarios(id) ON DELETE CASCADE,
                               rol_id INT REFERENCES roles(id) ON DELETE CASCADE,
                               PRIMARY KEY (usuario_id, rol_id)
);

-- =========================
-- Tabla: especialidades
-- =========================
-- Almacena las especialidades médicas disponibles.
-- Ejemplo: Cardiología, Pediatría, Nutrición.
CREATE TABLE especialidades (
                                id SERIAL PRIMARY KEY,
                                nombre VARCHAR(100) UNIQUE NOT NULL,
                                descripcion TEXT
);

-- =========================
-- Tabla: profesionales
-- =========================
-- Información específica de usuarios que son profesionales de la salud.
-- Cada profesional se asocia a un usuario y opcionalmente a una especialidad.
CREATE TABLE profesionales (
                               id SERIAL PRIMARY KEY,
                               usuario_id INT REFERENCES usuarios(id) ON DELETE CASCADE,        -- FK a usuarios
                               especialidad_id INT REFERENCES especialidades(id) ON DELETE SET NULL, -- FK a especialidades
                               colegiatura VARCHAR(50),                                        -- Número de colegiatura profesional
                               telefono VARCHAR(20)                                            -- Teléfono de contacto
);

-- =========================
-- Tabla: password_reset_token
-- =========================
-- Guarda los tokens para recuperación de contraseña.
-- Cada token está asociado a un usuario, tiene fecha de expiración y flag de usado.
CREATE TABLE password_reset_token (
                                      id SERIAL PRIMARY KEY,
                                      usuario_id INT REFERENCES usuarios(id) ON DELETE CASCADE,  -- FK a usuarios
                                      token VARCHAR(255) UNIQUE NOT NULL,                        -- Token único
                                      expiracion TIMESTAMP NOT NULL,                             -- Fecha de expiración del token
                                      usado BOOLEAN DEFAULT FALSE,                               -- Indica si ya fue usado
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Tabla: permisos
-- =========================
-- Lista de permisos que se pueden asignar a roles para controlar acceso a funcionalidades.
CREATE TABLE permisos (
                          id SERIAL PRIMARY KEY,
                          nombre VARCHAR(100) UNIQUE NOT NULL,   -- Nombre del permiso
                          descripcion TEXT                        -- Descripción opcional
);

-- =========================
-- Tabla: rol_permisos
-- =========================
-- Relación N:M entre roles y permisos.
-- Permite que un rol tenga múltiples permisos y un permiso pertenezca a múltiples roles.
CREATE TABLE rol_permisos (
                              rol_id INT REFERENCES roles(id) ON DELETE CASCADE,
                              permiso_id INT REFERENCES permisos(id) ON DELETE CASCADE,
                              PRIMARY KEY (rol_id, permiso_id)
);

-- =========================================================
-- Notas:
-- 1. ON DELETE CASCADE asegura que si un usuario o rol se elimina,
--    las relaciones asociadas se eliminen automáticamente.
-- 2. ON DELETE SET NULL en profesionales.especialidad_id permite mantener
--    al profesional aunque se elimine la especialidad.
-- 3. Las relaciones N:M se manejan mediante tablas intermedias (usuario_roles, rol_permisos).
-- 4. Esta estructura permite:
--    - Autenticación de usuarios.
--    - Autorización basada en roles y permisos.
--    - Gestión de profesionales y especialidades.
--    - Recuperación de contraseñas mediante tokens.
-- =========================================================
