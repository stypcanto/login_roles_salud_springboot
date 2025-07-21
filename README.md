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

## 🔧 Versiones utilizadas

Para lograr que Tailwind CSS funcionara correctamente con React + Vite, fue necesario alinear las versiones de las herramientas involucradas. Inicialmente se presentaron errores relacionados con la compatibilidad de versiones, especialmente al instalar Tailwind.

### ✔️ Versiones utilizadas exitosamente:

- **Node.js**: `v20.11.1`
- **npm**: `10.2.4`
- **Tailwind CSS**: `v3.4.1`
- **Vite**: `v5.2.8`
- **React**: `v18.2.0`

> ⚠️ **Problemas detectados:**
> Al instalar Tailwind con versiones antiguas de Node.js y npm, se presentaron errores como:
> - `Cannot find module 'tailwindcss'`
> - Problemas con el archivo `tailwind.config.js` que no se generaba correctamente.
> - `postcss` y `autoprefixer` no funcionaban al compilar.

### ✅ Solución aplicada:

1. **Actualización de Node.js y npm** a versiones modernas usando `nvm` o descarga oficial.
2. Instalación de Tailwind siguiendo la documentación oficial para Vite:
   ```bash
   npm install -D tailwindcss postcss autoprefixer
   npx tailwindcss init -p


## 🚀 ¿Cómo ejecutarlo?

```bash

# Paso 1: Apagar contenedores si están activos
docker-compose down

# Paso 2: Iniciar todo en segundo plano (con build incluido)
docker-compose up --build -d

```

## ✅ Estado actual de la plantilla

- 🐳 Docker Compose funciona con **frontend** y **backend** sin errores.
- ⚙️ Backend **Spring Boot** responde correctamente en: [http://localhost:8080](http://localhost:8080)
- 🖥️ Frontend **React (Vite)** sirve correctamente desde Docker: [http://localhost:3000](http://localhost:3000)
- 🎨 **Tailwind CSS** y **Vite** están compilando sin problemas.
- 🔄 La comunicación entre **frontend y backend** está preparada para ser implementada con llamadas `fetch` o librerías como `axios`.

> Nota: Puedes conectarte a la base de datos con un cliente como DBeaver o pgAdmin usando:
>
> - Host: `localhost`
> - Puerto: `5432`
> - Usuario: `postgres`
> - Contraseña: `postgres`
> - Base de datos: `mydb`


## ⚙️ Arquitectura del sistema

- **React (Vite)** renderiza la interfaz del usuario.
- **Spring Boot** ofrece un backend RESTful conectado a la base de datos.
- **PostgreSQL** se usa como sistema gestor de base de datos relacional.
- **Docker** se encarga de crear contenedores independientes para cada componente.

Cada servicio está encapsulado y comunicado por Docker (ver `docker-compose.yml`).

---

## 🧩 Sobre el archivo `.env`

### 📍 ¿Dónde está?

- El archivo .env está ubicado en frontend/.env

### 📍¿Para qué sirve?

Define variables de entorno del frontend. En esta plantilla se configura la URL del backend:

```bash
VITE_API_URL=http://localhost:8080
```

### 📍 ¿Cómo se usa?
En React (Vite), accedes así:

```js
const apiUrl = import.meta.env.VITE_API_URL;
```

🔥 Recomendación: Nunca hagas hardcode de rutas. Usa siempre el .env.

### 📁 Archivo de configuración API - `api.js`

Para mantener una estructura limpia y facilitar futuras modificaciones, he creado el archivo:


### ¿Para qué sirve?

Este archivo centraliza la URL base del backend, tomando la variable desde el archivo `.env`. Esto es muy útil porque permite que el frontend se conecte dinámicamente a diferentes entornos (desarrollo, producción, etc.) sin tener que modificar múltiples archivos.

### Código dentro del archivo `api.js`:

```js
export const apiUrl = import.meta.env.VITE_API_URL;
````

De esta manera, el día que necesite llamar al backend, simplemente puedo hacer:

```js
import { apiUrl } from '../config/api';

fetch(`${apiUrl}/usuarios`)
  .then(response => response.json())
  .then(data => {
    console.log('Usuarios:', data);
  });

```

✅ Ventaja: Si más adelante quiero reutilizar esta plantilla en otro proyecto, solo necesito cambiar la variable VITE_API_URL en el .env sin tocar nada más en el código.

Este archivo api.js quedará como base para futuras conexiones API que usarán esta plantilla.




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
