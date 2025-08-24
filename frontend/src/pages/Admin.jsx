import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Admin() {
    const [roles, setRoles] = useState([]);
    const navigate = useNavigate();

    // Mapa de roles a rutas
    const roleRoutes = {
        Superadmin: "/roles",           // Página de administración
        Administrador: "/roles",        // Página de administración
        "Coordinador Medico": "/CoordinadorEspecialidades",
        Medico: "/PortalMedico",
        "Coordinador Admision": "/GestionTerritorial"
    };

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            navigate("/admin"); // Redirige al login si no hay token
            return;
        }

        // Obtener roles del localStorage
        const storedRoles = JSON.parse(localStorage.getItem("roles")) || [];
        setRoles(storedRoles.map(r => r.nombre)); // Solo nombres de roles
    }, [navigate]);

    const handleRoleClick = (rol) => {
        const ruta = roleRoutes[rol];
        if (ruta) {
            navigate(ruta);
        } else {
            navigate("/"); // fallback a página genérica o 404
        }
    };

    if (roles.length === 0) {
        return (
            <p className="text-center text-gray-700 mt-10">
                No tienes roles asignados.
            </p>
        );
    }

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 p-6">
            <h1 className="text-2xl font-bold mb-6 text-blue-900">Selecciona tu Portal</h1>
            <ul className="space-y-4 w-full max-w-md">
                {roles.map((rol, index) => (
                    <li key={index}>
                        <button
                            onClick={() => handleRoleClick(rol)}
                            className="w-full px-6 py-3 rounded-lg bg-blue-600 text-white font-semibold hover:bg-blue-700 transition"
                        >
                            {rol}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
