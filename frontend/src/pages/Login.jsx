import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../config/api.js";

const Login = () => {
  const [correo, setCorreo] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  // Mapa de roles a rutas
  const roleRoutes = {
    Superadmin: "/admin-panel",
    Administrador: "/admin-panel",
    "Coordinador Medico": "/CoordinadorEspecialidades",
    Medico: "/PortalMedico",
    "Coordinador Admision": "/GestionTerritorial",
  };

  const handleAdmin = async (e) => {
    e.preventDefault();
    setError(null);

    if (!correo || !password) {
      setError("Todos los campos son obligatorios");
      return;
    }

    try {
      setIsLoading(true);

      const res = await api.post("/auth/login", {
        correo,
        contrasena: password,
      });

      const { token, roles, profesional } = res.data;

      if (!token) {
        setError("Credenciales incorrectas");
        return;
      }

      // Guardamos datos en localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("roles", JSON.stringify(roles));
      localStorage.setItem("profesional", JSON.stringify(profesional));

      // Redirección según rol principal
      const rolPrincipal = roles[0]?.nombre;
      const ruta = roleRoutes[rolPrincipal] || "/"; // fallback
      navigate(ruta);

    } catch (err) {
      console.error("Error de conexión:", err);

      if (err.response) {
        setError(err.response.data || "Credenciales incorrectas");
      } else if (err.request) {
        setError("No se pudo conectar con el servidor");
      } else {
        setError("Ocurrió un error inesperado");
      }

    } finally {
      setIsLoading(false);
    }
  };

  return (
      <div className="min-h-screen bg-cover bg-center bg-[url('/images/fondo-portal-web-cenate-2025.png')] flex items-center justify-center">
        <div className="w-full max-w-md p-8 bg-white rounded-lg shadow-lg">
          <h1 className="mb-6 text-3xl font-semibold text-center text-blue-900">
            Login CENATE
          </h1>

          {error && <p className="mb-4 text-center text-red-500">{error}</p>}

          <form onSubmit={handleAdmin}>
            <div className="mb-4">
              <input
                  type="email"
                  placeholder="Correo electrónico"
                  value={correo}
                  onChange={(e) => setCorreo(e.target.value)}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-6">
              <input
                  type="password"
                  placeholder="Contraseña"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

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

          <div className="flex items-center justify-center mt-4">
            <Link
                to="/forgot-password"
                className="text-sm text-[#2e63a6] hover:text-blue-500 underline"
            >
              ¿Olvidaste tu contraseña?
            </Link>
          </div>

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
