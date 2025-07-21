# 🧠 Plantilla Base: React + Spring Boot + PostgreSQL + Docker

## 🧱 ¿Qué es esto?
Esta es una plantilla base de desarrollo fullstack que integra:

- Frontend: React + Vite + Tailwind CSS
- Backend: Spring Boot (Java)
- Base de datos: PostgreSQL
- Contenedores: Docker + Docker Compose

### ⚙️ Ideal para comenzar cualquier aplicación web modular y escalable en segundos.

## 📁 Estructura del proyecto

```bash
1.Proyecto_React_Java/
│
├── backend/                 # Backend en Spring Boot
│   ├── src/main/java/...
│   └── Dockerfile
│
├── frontend/                # Frontend en React + Tailwind
│   ├── src/
│   ├── public/
│   ├── Dockerfile
│   └── .env                 # Variables frontend
│
├── docker-compose.yml       # Orquesta todos los servicios
└── README.md                # Documentación del proyecto
```

## 🚀 ¿Cómo ejecutarlo?

```bash

# Paso 1: Apagar contenedores si están activos
docker-compose down

# Paso 2: Iniciar todo en segundo plano (con build incluido)
docker-compose up --build -d

```

### Accesos:

- Frontend: http://localhost:3000
- Backend (API REST): http://localhost:8080
- PostgreSQL: puerto 5432 (interno)

## ⚙️ Arquitectura del sistema

- **React (Vite)** renderiza la interfaz del usuario.
- **Spring Boot** ofrece un backend RESTful conectado a la base de datos.
- **PostgreSQL** se usa como sistema gestor de base de datos relacional.
- **Docker** se encarga de crear contenedores independientes para cada componente.

Cada servicio está encapsulado y comunicado por Docker (ver `docker-compose.yml`).

---

## 🧩 Sobre el archivo `.env`

# 📍 ¿Dónde está?

- El archivo .env está ubicado en frontend/.env

### ¿Para qué sirve?

Define variables de entorno del frontend. En esta plantilla se configura la URL del backend:

```bash
VITE_API_URL=http://localhost:8080
```

## ¿Cómo se usa?
En React (Vite), accedes así:

```js
const apiUrl = import.meta.env.VITE_API_URL;
```

🔥 Recomendación: Nunca hagas hardcode de rutas. Usa siempre el .env.


## 🔄 ¿Cómo escalar esta plantilla en el futuro?

- Añadir rutas REST al backend (/api/usuarios, /api/citas, etc.)
- Conectar el frontend al backend con fetch() o axios
- Crear tablas y relaciones en PostgreSQL
- Agregar autenticación (JWT, OAuth)
- Separar entorno producción y desarrollo con múltiples .env

## 🛠️ Tareas futuras

## ✅ Añadir rutas REST al backend

Se recomienda extender el backend con nuevas rutas para manejar recursos clave del sistema. Algunas rutas sugeridas son:

- `GET /api/usuarios` – Listar todos los usuarios.
- `POST /api/usuarios` – Crear un nuevo usuario.
- `GET /api/citas` – Obtener todas las citas registradas.
- `POST /api/citas` – Registrar una nueva cita médica.
- `PUT /api/citas/{id}` – Actualizar una cita existente.
- `DELETE /api/citas/{id}` – Eliminar una cita.

Estas rutas deben implementarse en controladores Spring Boot bajo el prefijo `/api` para mantener una estructura RESTful clara y consistente.


## ✅ Comandos útiles

```bash

# Ver contenedores activos
docker ps

# Apagar todo
docker-compose down

# Ver logs en tiempo real
docker-compose logs -f

# Reconstruir todo y ejecutar en segundo plano
docker-compose up --build -d

````

## ✍️ Autor

**Nombre:** Styp Canto

**Objetivo del proyecto:**  
Usar como plantilla para futuros desarrollos en **React + Spring Boot**.
