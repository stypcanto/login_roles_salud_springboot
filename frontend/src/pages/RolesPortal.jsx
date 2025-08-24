import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function RolesPortal() {
    const [roles, setRoles] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // Verificar token
        const token = localStorage.getItem("token");
        if (!token) {
            navigate("/admin"); // Redirige al login si no hay token
            return;
        }

        // Obtener roles del localStorage
        const storedRoles = JSON.parse(localStorage.getItem("roles")) || [];
        setRoles(storedRoles.map((r) => r.nombre)); // Solo nombres de roles
    }, [navigate]);

    // Función para navegar según el rol
    const handleRoleClick = (rol) => {
        switch (rol) {
            case "Superadmin":
            case "Administrador":
                navigate("/RolesPortal"); // O tu ruta de administración
                break;
            case "Coordinador Medico":
                navigate("/CoordinadorEspecialidades");
                break;
            case "Medico":
                navigate("/PortalMedico");
                break;
            case "Coordinador Admision":
                navigate("/GestionTerritorial");
                break;
            default:
                navigate("/"); // Ruta genérica o 404
                break;
        }
    };

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 p-6">
            <h1 className="text-2xl font-bold mb-6 text-blue-900">Selecciona tu Portal</h1>
            {roles.length === 0 ? (
                <p className="text-gray-700">No tienes roles asignados.</p>
            ) : (
                <ul className="space-y-4">
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
            )}
        </div>
    );
}
