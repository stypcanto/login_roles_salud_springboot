import axios from 'axios';

// Usa la variable de entorno VITE_API_URL definida en el Dockerfile o .env
const api = axios.create({
  baseURL: `${import.meta.env.VITE_API_URL}/api`,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
