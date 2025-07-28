# 🚀 Plantilla Base Fullstack: React + Spring Boot + PostgreSQL + Docker

Este repositorio proporciona una arquitectura moderna y modular para el desarrollo de aplicaciones web completas (**fullstack**) utilizando contenedores Docker.

---

## 🛠️ Tecnologías Utilizadas

| Capa        | Tecnología                            | Descripción                                        |
|-------------|----------------------------------------|----------------------------------------------------|
| Frontend    | [React](https://reactjs.org)           | Biblioteca de JavaScript para construir interfaces |
|             | [Vite](https://vitejs.dev)             | Bundler moderno para React                         |
|             | [Tailwind CSS](https://tailwindcss.com)| Framework de utilidades CSS para diseño UI rápido  |
| Backend     | [Spring Boot 3.x](https://spring.io/projects/spring-boot) | Framework Java basado en Spring (con Spring Web MVC y Spring Data JPA) |
|             | [Java 17](https://openjdk.org/projects/jdk/17/) | Versión LTS utilizada                              |
| Base de Datos | [PostgreSQL 15+](https://www.postgresql.org/) | Sistema de base de datos relacional                |
| Contenedores| [Docker](https://www.docker.com/)      | Contenerización de servicios                       |
| Orquestación| [Docker Compose](https://docs.docker.com/compose/) | Levanta toda la app con un solo comando           |

---

## 🧱 Estructura del Proyecto

```bash
3.Proyecto_Login_Springboot/
│
├── backend/ # Proyecto Spring Boot (Java)
│ ├── Dockerfile
│ └── src/main/...
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

## 💡 Posibilidades Futuras

### 🔐 Seguridad y Autenticación
- Implementar autenticación con **JWT** o **OAuth2** (Google, GitHub, etc.)
- Validaciones backend con Spring Security

### 🎛️ Panel y Roles
- Crear un **panel de administración** con control de acceso
- Definir roles: administrador, coordinador, usuario, etc.
- Permisos granulares para rutas y componentes

### 📬 Notificaciones
- Envío de **correos electrónicos** con Mailgun o SMTP
- Notificaciones push o en tiempo real con WebSockets o Firebase

### 🧪 Testing y Calidad
- Pruebas unitarias con **JUnit** (backend) y **Jest/React Testing Library** (frontend)
- Pruebas de integración con Postman/Newman o Testcontainers
- Análisis de código con SonarQube

### 🚀 Despliegue y CI/CD
- Automatizar despliegue con **GitHub Actions**
- Deploy en **Render**, **Railway**, **Vercel**, **Heroku** o **AWS EC2**
- Versionamiento semántico (semver) + tags de release

### ⚙️ Optimización y Mantenimiento
- Uso de **Docker multi-stage builds** para reducir peso
- Caching en frontend y backend
- Monitorización con Prometheus + Grafana o Sentry

### 🔄 Integraciones Externas
- API REST para terceros
- Conexión con servicios como Twilio, Stripe o Google Calendar

### 🗃️ Base de Datos y Persistencia
- Migraciones con Flyway o Liquibase
- Soporte para múltiples entornos: dev, staging, prod

### 📱 Mobile & PWA
- Convertir el frontend en **Progressive Web App**
- Explorar desarrollo móvil con React Native compartiendo lógica

---

## 👨‍💻 Autor

**Styp Canto**  
🧠 Stack: Fullstack Java + React  
🎯 Propósito: Tener una **base sólida, profesional y escalable** para futuros desarrollos web.
