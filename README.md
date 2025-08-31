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
| Seguridad       | Spring Security + JWT futuro   |
| Base de datos   | PostgreSQL 15+                 |
| Frontend        | React + Vite + Tailwind CSS   |
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
▶️ Ejecución
Con Docker Compose
```bash
docker-compose down
docker-compose up --build -d

```
Backend: http://localhost:8080

Frontend: http://localhost:5173

PostgreSQL: puerto 5432 (interno)

### Pruebas rápidas con curl

```bash
# Healthcheck
curl http://localhost:8080/health

# Registro de usuario
curl -X POST http://localhost:8080/auth/register \
-H "Content-Type: application/json" \
-d '{"correo":"usuario@ejemplo.com","contrasena":"123456"}'

# Login
curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{"correo":"styp611@outlook.com", "contrasena":"123456"}'

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

Debe salir las credenciales de la autenticacion registradas por .env:
```bash
jdbc:postgresql://db:5432/mydb
postgres
postgres
styp@MacBookPro-2622 3.Proyecto_Login_Springboot % 
```

## Relaciones entre tablas de usuarios, roles y profesionales

No es necesario crear llaves foráneas adicionales entre `profesionales` y `usuario_roles`. La razón es la siguiente:

- La tabla `profesionales` ya tiene la columna `usuario_id` que referencia a `usuarios(id)` ✅
- La tabla `usuario_roles` también referencia a `usuarios(id)` ✅

Esto significa que la relación **profesional → roles** ya existe implícitamente a través de la tabla `usuarios`:

profesionales.usuario_id → usuarios.id → usuario_roles.usuario_id → roles.id


Agregar una llave foránea directa de `profesionales` a `usuario_roles` sería **redundante** y podría generar inconsistencias, porque `usuario_roles` puede tener múltiples filas por usuario y no hay un único rol por profesional.

La forma correcta de manejar la relación es mediante **JOINs** en las consultas SQL. Por ejemplo, para obtener los roles de cada profesional:

```sql
SELECT p.nombres, p.apellidos, r.nombre AS rol
FROM profesionales p
JOIN usuarios u ON p.usuario_id = u.id
JOIN usuario_roles ur ON ur.usuario_id = u.id
JOIN roles r ON r.id = ur.rol_id;
```
✅ Esta estrategia mantiene la integridad referencial y evita relaciones duplicadas innecesarias.


## ✅ Control de usuarios activos


La columna `activo` en la tabla `usuarios` indica si un usuario está habilitado para autenticarse:

- `true` → usuario activo, puede iniciar sesión ✅
- `false` → usuario inactivo, no puede iniciar sesión ⚠️

**Importancia:**

- Permite deshabilitar cuentas temporal o permanentemente sin eliminarlas.
- Mejora la seguridad y el control administrativo.
- Evita que usuarios con roles asignados accedan si su cuenta está desactivada.

En la lógica de login, se valida que `activo = true` antes de generar el token JWT.

```sql

-- Ver todos los usuarios activos
SELECT id, correo, activo
FROM usuarios
WHERE activo = true;

```

Para conectarme a mi base de datos desde consola:

```sql
docker exec -it 3proyecto_login_springboot-db-1 psql -U postgres -d mydb
```

El enlace para probar la api de profesionales:

```http request
http://localhost:8080/api/profesionales
```
Este me dará un json como
```json
[
{
"id": 2,
"nombres": "Maria",
"apellidos": "Lopez",
"documento": "23456789",
"colegiatura": "C002",
"especialidad": "Pediatría",
"ipressId": 2,
"telefono": "987654322",
"email": null,
"activo": true,
"createdAt": "2025-08-22T00:02:27.868584",
"updatedAt": "2025-08-22T00:02:27.868584",
"tipoDocumento": "DNI",
"usuarioId": 5,
"fechaNacimiento": null
},
{
"id": 3,
"nombres": "Carlos",
"apellidos": "Gomez",
"documento": "E1234567",
"colegiatura": "C003",
"especialidad": "Ginecología",
"ipressId": 1,
"telefono": "987654323",
"email": null,
"activo": true,
"createdAt": "2025-08-22T00:02:27.868584",
"updatedAt": "2025-08-22T00:02:27.868584",
"tipoDocumento": "C.Extranjería",
"usuarioId": 6,
"fechaNacimiento": null
},
{
"id": 4,
"nombres": "Ana",
"apellidos": "Martinez",
"documento": "34567890",
"colegiatura": "C004",
"especialidad": "Cardiología",
"ipressId": 3,
"telefono": "987654324",
"email": null,
"activo": true,
"createdAt": "2025-08-22T00:02:27.868584",
"updatedAt": "2025-08-22T00:02:27.868584",
"tipoDocumento": "DNI",
"usuarioId": null,
"fechaNacimiento": null
},

]
```
## Arquitectura de Usuarios y Profesionales

Este proyecto está diseñado para ser **real, escalable y seguro**, manteniendo una clara separación entre `usuarios` y `profesionales`.

### Estructura de Tablas

- **usuarios**: Maneja la **autenticación**, los **roles** y el **estado** (`activo`/`inactivo`).
- **profesionales**: Contiene la información específica del profesional, como **nombre**, **especialidad**, **RNE**, etc., y está vinculada a un usuario existente.

### Buenas Prácticas

#### 1. Validar la relación al crear profesionales
- Asegúrate de que `usuario_id` existe y tiene el rol adecuado (`Profesional`).
- Esto evita inconsistencias en la tabla `usuario_roles`.

#### 2. Automatizar la asignación de roles
- Al crear un profesional, si el usuario aún no tiene el rol “Profesional”, asignarlo automáticamente en `usuario_roles`.
- Así se evita la manipulación manual de la tabla intermedia.

#### 3. DTOs claros para el frontend
- Combina los datos de `usuario` y `profesional` en un solo DTO (`ProfesionalDTO`).
- Esto permite al frontend obtener toda la información sin hacer joins complejos ni preocuparse por los roles.

#### 4. Evitar campos innecesarios en `usuario`
- Mantén en `usuario` solo los campos relacionados con **autenticación y roles**.
- Toda la información específica del profesional se gestiona en la tabla `profesionales`.

### 💡 Resumen práctico

- Mantén las tablas separadas: `usuarios` y `profesionales`.
- Automatiza la asignación de roles.
- Usa DTOs combinados para el frontend.
- No elimines la tabla intermedia `usuario_roles` si quieres que un usuario pueda tener múltiples roles.
