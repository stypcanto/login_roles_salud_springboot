import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL, // Ejemplo: http://localhost/api o http://backend:8080/api
  headers: { 'Content-Type': 'application/json' }
});

// Interceptor para agregar el JWT automáticamente
api.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('token'); // obtener token del login
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
);

export default api;
