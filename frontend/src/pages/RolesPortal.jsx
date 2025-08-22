import { useEffect, useState } from "react";
import api from "../config/api"; // tu api.js
import { useNavigate } from "react-router-dom";

export default function RolesPortal() {
    const [roles, setRoles] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token"); // el token que guardaste en login
        if (!token) {
            navigate("/login");
            return;
        }

        // Llamada protegida con Authorization
        api.get("/portaladmin", {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
            .then((res) => setRoles(res.data))
            .catch((err) => {
                console.error("Error al obtener roles:", err);
                if (err.response?.status === 403 || err.response?.status === 401) {
                    localStorage.removeItem("token");
                    navigate("/login");
                }
            });
    }, [navigate]);

    return (
        <div className="p-6">
            <h1 className="text-xl font-bold mb-4">Selecciona tu Portal</h1>
            <ul className="space-y-2">
                {roles.map((rol, i) => (
                    <li key={i}>
                        <button
                            onClick={() => navigate(`/${rol.toLowerCase()}`)}
                            className="px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-600"
                        >
                            {rol}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
