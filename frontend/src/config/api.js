import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL, // Ejemplo: http://localhost/api o http://backend:8080/api
  headers: { 'Content-Type': 'application/json' }
});

export default api;
