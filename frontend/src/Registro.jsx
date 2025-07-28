import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function Registro() {
  const navigate = useNavigate();
  const [correo, setCorreo] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [mensaje, setMensaje] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleRegistro = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    
    try {
      await axios.post("http://localhost:8080/api/auth/register", {
        correo,
        contrasena,
      });

      setMensaje("✅ Usuario registrado correctamente.");
      setTimeout(() => navigate("/"), 2000);
    } catch (error) {
      const errorMessage = error.response?.data?.message || 
                         error.response?.data || 
                         "Error al conectar con el servidor";
      setMensaje(`❌ ${errorMessage}`);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen px-4 bg-gray-100">
      <form
        onSubmit={handleRegistro}
        className="w-full max-w-md p-6 space-y-4 bg-white rounded-lg shadow-md"
      >
        <h2 className="text-2xl font-bold text-center">Crear Cuenta</h2>
        
        <div className="space-y-2">
          <input
            type="email"
            placeholder="Correo electrónico"
            value={correo}
            onChange={(e) => setCorreo(e.target.value)}
            className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            required
          />
          
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
        
        <button
          type="submit"
          disabled={isLoading}
          className={`w-full p-2 text-white rounded transition-colors ${
            isLoading ? "bg-blue-400 cursor-not-allowed" : "bg-blue-600 hover:bg-blue-700"
          }`}
        >
          {isLoading ? "Registrando..." : "Registrarse"}
        </button>
        
        {mensaje && (
          <p className={`mt-2 text-sm text-center ${
            mensaje.includes("✅") ? "text-green-600" : "text-red-600"
          }`}>
            {mensaje}
          </p>
        )}
      </form>
    </div>
  );
}

export default Registro;