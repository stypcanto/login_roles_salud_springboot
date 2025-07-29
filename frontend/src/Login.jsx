import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

const Login = () => {
  // Estados locales para inputs y estado de error/carga
  const [correo, setCorreo] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  // Función que maneja el inicio de sesión
  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null); // Limpia errores anteriores

    // Validación básica
    if (!correo || !password) {
      setError("Todos los campos son obligatorios");
      return;
    }

    try {
      setIsLoading(true); // Muestra el estado de carga

      // Enviar datos al backend usando axios
      const res = await axios.post(
        `${import.meta.env.VITE_API_URL}/api/auth/login`,
        { correo, contrasena: password }, // Nota: el backend espera 'contrasena'
        { headers: { "Content-Type": "application/json" } }
      );

      /**
       * Esperamos que el backend retorne algo así:
       * {
       *   success: true,
       *   message: "Inicio de sesión exitoso"
       * }
       */
      const { success, message } = res.data;

      if (success) {
        // Si el login fue exitoso, redirige al dashboard
        navigate("/dashboard");
      } else {
        // Si 'success' es false, muestra el mensaje del backend
        setError(message || "Credenciales incorrectas");
      }
    } catch (err) {
      console.error("Error de conexión:", err);
      // Si hay respuesta del backend, muestra el mensaje
      if (err.response?.data?.message) {
        setError(err.response.data.message);
      } else {
        setError("Error al conectar con el servidor");
      }
    } finally {
      setIsLoading(false); // Quita el estado de carga
    }
  };

  return (
    <div className="min-h-screen bg-cover bg-center bg-[url('/images/fondo-portal-web-cenate-2025.png')] flex items-center justify-center">
      <div className="w-full max-w-md p-8 bg-white rounded-lg shadow-lg">
        <h1 className="mb-6 text-3xl font-semibold text-center text-blue-900">
          Login CENATE
        </h1>

        {error && (
          <p className="mb-4 text-center text-red-500">{error}</p>
        )}

        <form onSubmit={handleLogin}>
          {/* Campo de correo */}
          <div className="mb-4">
            <input
              type="email"
              placeholder="Correo electrónico"
              value={correo}
              onChange={(e) => setCorreo(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          {/* Campo de contraseña */}
          <div className="mb-6">
            <input
              type="password"
              placeholder="Contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          {/* Botón de enviar */}
          <button
            type="submit"
            disabled={isLoading}
            className={`w-full py-3 px-6 bg-[#2e63a6] text-white text-sm font-semibold rounded-lg shadow-md hover:scale-105 focus:outline-none focus:ring-2 ${
              isLoading ? "opacity-50 cursor-not-allowed" : "hover:bg-[#2e63a6]"
            }`}
          >
            {isLoading ? "Cargando..." : "Iniciar sesión"}
          </button>
        </form>

        {/* Enlace para recuperar contraseña */}
        <div className="flex items-center justify-center mt-4">
          <Link
            to="/forgot-password"
            className="text-sm text-[#2e63a6] hover:text-blue-500 underline"
          >
            ¿Olvidaste tu contraseña?
          </Link>
        </div>

        {/* Enlace para registrarse */}
        <div className="mt-6 text-center">
          <p className="text-sm text-gray-700">
            ¿No tienes una cuenta?{" "}
            <span
              className="text-[#2e63a6] cursor-pointer font-semibold"
              onClick={() => navigate("/registro")}
            >
              Únete gratis
            </span>
          </p>
        </div>

        {/* Botón alternativo de registro */}
        <button
          type="button"
          onClick={() => navigate("/registro")}
          className="w-full p-3 mt-4 text-white bg-green-700 rounded-lg hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          Crear una cuenta
        </button>
      </div>
    </div>
  );
};

export default Login;
