# Fase de build
FROM node:20-alpine AS builder

WORKDIR /app

# Copiar dependencias
COPY package*.json ./
RUN npm install

# Copiar el resto del código
COPY . .

# Variable de entorno en tiempo de build para Vite
ARG VITE_API_URL
ENV VITE_API_URL=${VITE_API_URL}

# Confirmar que la variable existe
RUN echo "🛠️ Construyendo con VITE_API_URL=$VITE_API_URL"

# Construir el frontend con la variable incluida
RUN npm run build

# Fase de producción: NGINX
FROM nginx:alpine

# Copiar archivos construidos al servidor
COPY --from=builder /app/dist /usr/share/nginx/html

# Configuración NGINX (para SPA o proxy API)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
