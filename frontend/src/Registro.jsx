import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

// Componente funcional para registrar nuevos usuarios
function Registro() {
  // Hook para redireccionar al login después del registro exitoso
  const navigate = useNavigate();

  // Estados locales para los campos del formulario y control de estado
  const [correo, setCorreo] = useState("");               // Correo ingresado
  const [contrasena, setContrasena] = useState("");       // Contraseña ingresada
  const [mensaje, setMensaje] = useState("");             // Mensaje informativo (éxito o error)
  const [isLoading, setIsLoading] = useState(false);      // Indica si se está procesando el registro

  // Función que maneja el envío del formulario
  const handleRegistro = async (e) => {
    e.preventDefault();         // Previene el recargado del formulario
    setIsLoading(true);         // Inicia la animación de carga

    try {
      // Enviamos los datos al backend (POST /api/auth/register)
      await axios.post("/api/auth/register", {
        correo,
        contrasena,
      });

      // Si no lanza error, consideramos éxito
      setMensaje("✅ Usuario registrado correctamente.");
      
      // Redirigimos al login tras un breve retardo
      setTimeout(() => {
        navigate("/");  // Ruta del login
      }, 2000);
    } catch (error) {
      // Manejo de errores del backend o red
      const errorMessage =
        error.response?.data?.message ||           // Si backend envía { message: '...' }
        (typeof error.response?.data === "string"
          ? error.response.data
          : "Error al conectar con el servidor");  // Otros tipos de errores

      setMensaje(`❌ ${errorMessage}`);
    } finally {
      setIsLoading(false);  // Terminamos animación de carga
    }
  };

  // Render del formulario de registro
  return (
    <div className="flex flex-col items-center justify-center min-h-screen px-4 bg-gray-100">
      <form
        onSubmit={handleRegistro}
        className="w-full max-w-md p-6 space-y-4 bg-white rounded-lg shadow-md"
      >
        <h2 className="text-2xl font-bold text-center">Crear Cuenta</h2>

        {/* Campos de entrada del formulario */}
        <div className="space-y-2">
          {/* Campo correo */}
          <input
            type="email"
            placeholder="Correo electrónico"
            value={correo}
            onChange={(e) => setCorreo(e.target.value)}
            className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            required
          />

          {/* Campo contraseña */}
          <input
            type="password"
            placeholder="Contraseña"
            value={contrasena}
            onChange={(e) => setContrasena(e.target.value)}
            className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            required
            minLength="6"
          />
        </div>

        {/* Botón para registrar */}
        <button
          type="submit"
          disabled={isLoading}
          className={`w-full p-2 text-white rounded transition-colors ${
            isLoading
              ? "bg-blue-400 cursor-not-allowed"
              : "bg-blue-600 hover:bg-blue-700"
          }`}
        >
          {isLoading ? "Registrando..." : "Registrarse"}
        </button>

        {/* Mensaje de éxito o error */}
        {mensaje && (
          <p
            className={`mt-2 text-sm text-center ${
              mensaje.includes("✅") ? "text-green-600" : "text-red-600"
            }`}
          >
            {mensaje}
          </p>
        )}
      </form>
    </div>
  );
}

export default Registro;
