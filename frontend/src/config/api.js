import axios from 'axios';

// api.js
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL, // http://localhost/api
  headers: { 'Content-Type': 'application/json' }
});

// Login.jsx
// eslint-disable-next-line no-undef
await api.post("/auth/login", { correo, contrasena: password });

export default api;
