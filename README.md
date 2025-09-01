# 🔐 Proyecto de Autenticación - Spring Boot + React + Docker

Sistema completo de autenticación de usuarios, con backend en Spring Boot, frontend en React + Vite + Tailwind CSS, PostgreSQL como base de datos y orquestación con Docker.
---

## 🎯 Objetivo

Implementar un sistema de autenticación seguro mediante correo y contraseña, con:

- Rutas protegidas
- JWT (JSON Web Tokens) para sesiones
- Recuperación de contraseña mediante token
- API REST escalable y mantenible
---

## ⚙️ Stack Tecnológico

| Componente      | Tecnología                     |
|----------------|--------------------------------|
| Backend         | Spring Boot 3.x (Java 17)     |
| Seguridad       | Spring Security + JWT          |
| Base de datos   | PostgreSQL 15+                 |
| Frontend        | React + Vite + Tailwind CSS    |
| Contenerización | Docker + Docker Compose       |

---

## 🔗 Endpoints Clave

| Endpoint               | Método | Descripción                         |
|------------------------|--------|-------------------------------------|
| `/health`              | GET    | Verifica estado del backend         |
| `/auth/ping`           | GET    | Ping de prueba de autenticación     |
| `/auth/register`       | POST   | Registrar nuevo usuario             |
| `/auth/login`          | POST   | Iniciar sesión con correo y contraseña |
| `/auth/forgot-password`| POST   | Solicitar token de recuperación     |
| `/auth/reset-password` | POST   | Restablecer contraseña con token    |

---

## 🧱 Estructura del Proyecto

```bash
3.Proyecto_Login_Springboot/
├── backend/
│   ├── Dockerfile.prod
│   ├── Dockerfile.dev (opcional, si quieres hot reload backend)
│   └── src/
├── frontend/
│   ├── Dockerfile.prod
│   ├── Dockerfile.dev
│   ├── nginx.conf
│   └── src/
├── .env.development
├── .env.production
├── docker-compose.yml
├── docker-compose.override.yml
└── README.md


```
## ▶️ Ejecución
### Levantar solo producción
```bash
docker-compose -f docker-compose.yml --env-file .env.production up --build
```
- Usa tu Dockerfile.prod tanto en frontend como backend.
- Frontend servirá en puerto 80 con Nginx.
- Backend servirá en 8080.

### Levantar solo desarrollo

```bash
docker-compose --env-file .env.development up --build
```
- Usa tu docker-compose.override.yml y Dockerfile.dev.
- Frontend en 5173 con hot-reload de Vite.
- Backend en 8080, también con live reload si lo configuras.


## 🔄 Probando la API con curl

### Healthcheck
```bash
curl http://localhost:8080/health
```

### Registro de usuario
```bash
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{
  "correo": "usuario1@example.com",
  "contrasena": "MiPassword123",
  "nombres": "Juan",
  "apellidos": "Perez",
  "tipoDocumento": "DNI",
  "documento": "12345678",
  "telefono": "987654321",
  "roles": ["MEDICO", "COORDINADOR_MEDICO"]
}'

```
Consulta de base de datos para validar el usuario creado:
```sql
SELECT u.id AS usuario_id,
       u.correo,
       r.nombre AS rol
FROM usuario_roles ur
JOIN usuarios u ON ur.usuario_id = u.id
JOIN roles r ON ur.rol_id = r.id
WHERE u.correo = 'usuario1@example.com';

```

Para ver todos los usuarios con sus roles sin filtrar por correo:
```sql
SELECT 
    u.id AS usuario_id,
    u.correo,
    u.nombres,
    u.apellidos,
    STRING_AGG(r.nombre, ', ') AS roles
FROM usuarios u
LEFT JOIN usuario_roles ur ON u.id = ur.usuario_id
LEFT JOIN roles r ON ur.rol_id = r.id
GROUP BY u.id, u.correo, u.nombres, u.apellidos
ORDER BY u.id;

```

### Login
```bash

curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
  "correo": "guille@outlook.com",
  "contrasena": "admin123"
}'
```

### Probando la conexion de base de datos

Desde la raiz
```bash
cd /Users/styp/Documents/Cursos/Proyectos_Personales/3.Proyecto_Login_Springboot
export $(grep -v '^#' .env.development.production | xargs)


echo $SPRING_DATASOURCE_URL
echo $SPRING_DATASOURCE_USERNAME
echo $SPRING_DATASOURCE_PASSWORD

```

Para conectarte desde la consola de Docker:
```bash
docker exec -it 3proyecto_login_springboot-db-1 psql -U postgres -d mydb

```

## 🏗 Arquitectura de Usuarios y Profesionales

- **usuarios**: autenticación, roles y estado.
- **profesionales**: datos del profesional y vinculación con usuario.

### Buenas prácticas

1. Validar `usuario_id` y rol al crear un profesional.
2. Asignar automáticamente rol “Profesional” si no existe.
3. Usar DTOs combinados para el frontend (`ProfesionalDTO`).
4. Mantener `usuarios` solo con campos de autenticación y roles.

### 🔑 Claves de implementación

- `ProfesionalService` para todas las operaciones de negocio.
- Validación de profesionales existentes por usuario.
- Operaciones CRUD completas.
- Listado por especialidad y por usuario.
- Respuestas HTTP claras (404, 400, 500).
- Compatible con JWT, roles y permisos.
