import { useState } from "react";
import api from "./config/api";

const ForgotPassword = () => {
    const [correo, setCorreo] = useState("");
    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setMensaje("");

        try {
            const res = await api.post("/auth/forgot-password", { correo });
            setMensaje(res.data); // Aquí puedes mostrar el token directamente en modo dev
        } catch (err) {
            console.error(err);
            setError(err.response?.data || "Error al solicitar recuperación");
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center">
            <div className="p-6 bg-white rounded shadow-md w-full max-w-md">
                <h1 className="text-2xl mb-4">Recuperar Contraseña</h1>
                {error && <p className="text-red-500 mb-2">{error}</p>}
                {mensaje && <p className="text-green-500 mb-2">{mensaje}</p>}
                <form onSubmit={handleSubmit}>
                    <input
                        type="email"
                        placeholder="Correo electrónico"
                        value={correo}
                        onChange={(e) => setCorreo(e.target.value)}
                        className="w-full p-2 border rounded mb-4"
                    />
                    <button type="submit" className="w-full p-2 bg-blue-600 text-white rounded">
                        Enviar token
                    </button>
                </form>
            </div>
        </div>
    );
};

export default ForgotPassword;
