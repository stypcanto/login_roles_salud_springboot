import { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../config/api.js";

export default function ResetPassword() {
    const [newPassword, setNewPassword] = useState("");
    const [mensaje, setMensaje] = useState("");
    const [token, setToken] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    // Extraer token de la URL: /reset-password?token=...
    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const t = params.get("token");
        if (t) setToken(t);
        else setMensaje("Token no encontrado en la URL");
    }, [location]);

    const handleResetPassword = async (e) => {
        e.preventDefault();
        if (!token) return;

        try {
            const res = await api.post("/auth/reset-password", {
                token,
                newPassword,
            });
            setMensaje(res.data); // "Contraseña cambiada correctamente"
            setTimeout(() => navigate("/"), 2000); // Redirigir al login
        } catch (err) {
            setMensaje("Error al cambiar la contraseña");
            console.error(err);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center">
            <div className="w-full max-w-md p-8 bg-white rounded shadow">
                <h1 className="text-2xl mb-4">Restablecer Contraseña</h1>
                <form onSubmit={handleResetPassword}>
                    <input
                        type="password"
                        placeholder="Ingrese la nueva contraseña"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        className="w-full p-2 border mb-4 rounded"
                        required
                    />
                    <button className="w-full bg-green-600 text-white py-2 rounded">
                        Cambiar Contraseña
                    </button>
                </form>
                {mensaje && <p className="mt-4 text-red-500">{mensaje}</p>}
            </div>
        </div>
    );
}
