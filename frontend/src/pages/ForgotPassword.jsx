import { useState } from "react";
import api from "../config/api.js";

export default function ForgotPassword() {
    const [correo, setCorreo] = useState("");
    const [token, setToken] = useState(null);
    const [mensaje, setMensaje] = useState("");

    const handleForgotPassword = async (e) => {
        e.preventDefault();
        try {
            const res = await api.post("/auth/forgot-password", { correo });
            setMensaje(res.data); // "Token generado correctamente (modo desarrollo)"

            // Extraer el token del mensaje devuelto por el backend
            const tokenMatch = res.data.match(/[0-9a-f\-]{36}/);
            if (tokenMatch) {
                setToken(tokenMatch[0]);
            }
        } catch (err) {
            setMensaje("Error al generar token");
            console.error(err);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center">
            <div className="w-full max-w-md p-8 bg-white rounded shadow">
                <h1 className="text-2xl mb-4">Recuperar Contraseña</h1>
                <form onSubmit={handleForgotPassword}>
                    <input
                        type="email"
                        placeholder="Correo electrónico"
                        value={correo}
                        onChange={(e) => setCorreo(e.target.value)}
                        className="w-full p-2 border mb-4 rounded"
                        required
                    />
                    <button className="w-full bg-blue-600 text-white py-2 rounded">
                        Enviar
                    </button>
                </form>

                {mensaje && <p className="mt-4 text-red-500">{mensaje}</p>}

                {token && (
                    <p className="mt-4">
                        <a
                            href={`/reset-password?token=${token}`}
                            className="text-blue-700 underline"
                        >
                            Hacer clic aquí para restablecer la contraseña
                        </a>
                    </p>
                )}
            </div>
        </div>
    );
}
