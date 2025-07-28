# 🔐 Proyecto de Autenticación - Spring Boot + React + Docker

Este proyecto implementa un sistema de autenticación robusto utilizando Spring Boot para el backend, React para el frontend, PostgreSQL como base de datos, y Docker para la contenerización.

---

## 🎯 Objetivos Técnicos

- Implementar autenticación basada en correo y contraseña con cifrado.
- Gestionar recuperación y reinicio de contraseña por token temporal.
- Asegurar las rutas de API mediante Spring Security.
- Exponer endpoints RESTful seguros y validados.
- Integrar una arquitectura profesional y escalable.

---



## ⚙️ Tecnologías Usadas

| Componente      | Tecnología                           |
|----------------|---------------------------------------|
| Backend         | Spring Boot 3.x (Java 17)            |
| Seguridad       | Spring Security + JWT (en futuro)    |
| Base de datos   | PostgreSQL 15+                        |
| Frontend        | React + Vite + Tailwind CSS          |
| Contenerización | Docker + Docker Compose              |

---

## 🧱 Estructura del Proyecto

```bash
3.Proyecto_Login_Springboot/
│
├──backend/
├── controller/
│ └── AuthController.java
├── service/
│ ├── AuthService.java
│ └── PasswordResetService.java
├── repository/
│ └── UsuarioRepository.java
├── entity/
│ └── Usuario.java
├── config/
│ └── SecurityConfig.java
└── application.properties
│
├── frontend/ # Proyecto React (Vite + Tailwind)
│ ├── Dockerfile
│ └── src/
│
├── docker-compose.yml # Orquestador de servicios
└── README.md # Documentación técnica
```


---

## ⚙️ Configuraciones .env (Frontend)

Archivo: `frontend/.env`

```yaml
VITE_API_URL=http://localhost:8080
```

Uso en React:
```js
const apiUrl = import.meta.env.VITE_API_URL;
```

## ▶️ Instrucciones para ejecutar

```bash
# Detener contenedores antiguos
docker-compose down

# Construir e iniciar todo el entorno
docker-compose up --build -d

```

## 📦 Servicios Disponibles

| Servicio         | URL/Descripción                             |
|------------------|---------------------------------------------|
| 🖥️ **Frontend**   | [http://localhost:3000](http://localhost:3000) |
| 🧩 **Backend**    | [http://localhost:8080](http://localhost:8080) (API REST) |
| 🛢️ **PostgreSQL** | Expuesto internamente por Docker (puerto `5432`) |

---

## 🧩 Spring Boot: Componentes Incluidos

| Módulo                 | Descripción                                         |
|------------------------|-----------------------------------------------------|
| **Spring Web**         | Define los controladores REST (API REST)            |
| **Spring Data JPA**    | Abstracción de persistencia con PostgreSQL          |
| **Spring Boot Devtools** | Soporte para recarga en caliente (Hot Reload)      |
| **PostgreSQL Driver**  | Driver JDBC para conectarse a la base de datos      |

---

## 🧠 Arquitectura del Sistema

```text
               ┌──────────────────────────┐
               │      Usuario Final       │
               └──────────┬───────────────┘
                          │
                          ▼
         ┌────────────────────────────────────┐
         │        Frontend: React (Vite)      │
         │         + Tailwind CSS             │
         └────────────────┬───────────────────┘
                          │
                          ▼
         ┌────────────────────────────────────┐
         │       Backend: Spring Boot API     │
         └────────────────┬───────────────────┘
                          │
                          ▼
         ┌────────────────────────────────────┐
         │    PostgreSQL - Base de Datos      │
         └────────────────────────────────────┘

```  
✅ Cada servicio corre en su propio contenedor, orquestado por docker-compose.yml.

✅ Este entorno está preparado para desarrollo local y puede ser extendido fácilmente hacia entornos productivos.  
Puedes personalizar los puertos, credenciales y variables de entorno desde el archivo `.env`.

- Página de Login
![Diagrama de clases](docs/image0.png)

- Prueba exitosa de Login
![Diagrama de clases](docs/image2.png)

- Identificación de base de datos
![Diagrama de clases](docs/image3.png)

---


### 📚 Rutas REST comunes (ejemplo base)

```http
GET    /api/usuarios
POST   /api/usuarios
GET    /api/citas
POST   /api/citas
PUT    /api/citas/{id}
DELETE /api/citas/{id}

```

---

## 🧠 Recuperación de contraseña

Se genera una nueva estructura:

```plaintext
mi-proyecto/
├── backend/
│   ├── src/
│   │   ├── main/java/com/miempresa/miapp/
│   │   │   ├── controller/
│   │   │   │   └── AuthController.java         ← ENDPOINTS: forgot-password, reset-password
│   │   │   ├── entity/
│   │   │   │   └── PasswordResetToken.java     ← TOKEN con vencimiento
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   └── PasswordResetTokenRepository.java
│   │   │   ├── service/
│   │   │   │   └── EmailService.java           ← ENVÍA CORREO
│   │   │   └── model/                          ← (opcional) DTOs
│   │   └── resources/
│   │       └── application.properties          ← CONFIG MAIL SMTP
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── ForgotPassword.jsx              ← FORM ENVÍO DE CORREO
│   │   │   └── ResetPassword.jsx               ← FORM CAMBIO DE CONTRASEÑA
│   │   ├── App.jsx
│   │   ├── main.jsx
│   │   └── index.css
│   └── vite.config.js
│
├── docker-compose.yml                          ← (si usas contenedores)
└── README.md

```

Para entender la lógica de funconamiento, se diagramó el siguiente flujo:

```plaintext
[ForgotPassword.jsx] (usuario escribe su correo)
         │
         ▼
POST /api/auth/forgot-password ─────────► [AuthController.java]
                                               │
                                               ├── Busca al usuario
                                               ├── Genera token temporal
                                               ├── Guarda en BD (PasswordResetToken)
                                               └── Llama a EmailService para enviar correo
                                                           │
                                                           ▼
                                             [Token con URL enviado por Email]

         ▼
Usuario hace clic en el enlace: http://localhost:3000/reset-password?token=XYZ123

[ResetPassword.jsx] (usuario pone nueva contraseña)
         │
         ▼
POST /api/auth/reset-password ─────────► [AuthController.java]
                                               │
                                               ├── Valida token
                                               ├── Cambia la contraseña
                                               └── Elimina token

```

## 📌 Ideas a Futuro

A continuación se detallan posibles mejoras y extensiones técnicas del proyecto:

### 🔐 1. Autenticación con JWT (JSON Web Tokens)
- Reemplazar la autenticación basada en sesión por un sistema stateless con JWT.
- Firmar y validar tokens en cada request para proteger rutas privadas.
- Generar token de acceso y token de refresco (para renovar sesión sin volver a loguearse).

### 🖥️ 2. Frontend con React (Login y Recuperación)
- Crear un formulario de inicio de sesión totalmente funcional con manejo de errores.
- Implementar vistas para registro, recuperación de contraseña y restablecimiento con token.
- Gestionar estado de autenticación (tokens y expiración) desde el cliente.

### 📧 3. Notificaciones por Email (Producción)
- Integrar un proveedor de correo real como SendGrid, Mailgun o SMTP personalizado.
- Enviar token de recuperación vía email seguro.
- Incluir confirmaciones de registro y cambio de contraseña.

### 🛡️ 4. Roles y Permisos de Usuario
- Crear entidades `Rol` y `Permiso`, y relacionarlas con el usuario.
- Usar anotaciones como `@PreAuthorize("hasRole('ADMIN')")` para asegurar endpoints.
- Permitir distintas vistas y acciones en función del rol (Admin, Coordinador, Usuario).

### 📊 5. Auditoría de Inicios de Sesión
- Registrar eventos como login exitoso/fallido, cambios de contraseña, bloqueo por intentos fallidos.
- Guardar IP, navegador y timestamp.
- Visualizar el historial por usuario en un panel de administración.

### 📑 6. Documentación con Swagger / OpenAPI
- Integrar `springdoc-openapi` para generar documentación interactiva.
- Exponer rutas protegidas y públicas con descripciones claras.
- Probar las APIs desde Swagger UI (`/swagger-ui.html`) sin necesidad de Postman.
---

## 👨‍💻 Autor

**Styp Canto**  
🧠 Stack: Fullstack Java + React  
🎯 Propósito: Tener una **base sólida, profesional y escalable** para futuros desarrollos web.
