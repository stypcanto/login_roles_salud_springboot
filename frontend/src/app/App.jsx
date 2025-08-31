import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

// ==================== PÁGINAS PÚBLICAS ====================
import Login from "../pages/Login.jsx";
import Registro from "../pages/Registro.jsx";
import ForgotPassword from "../pages/ForgotPassword.jsx";
import ResetPassword from "../pages/ResetPassword.jsx";
import TestPing from "../pages/TestPing.jsx";
import NotFound from "../components/404/404.jsx";

// ==================== PÁGINAS PRIVADAS ====================
import Admin from "../pages/Admin.jsx";
import CoordinadorEspecialidades from "../pages/CoordinadorEspecialidades.jsx";
import PortalMedico from "../pages/PortalMedico.jsx";
import GestionTerritorial from "../pages/GestionTerritorial.jsx";
import PortalProfesionalSalud from "../pages/PortalProfesionalSalud.jsx";

// ==================== RUTAS PRIVADAS ====================
import PrivateRoute from "../components/Routes/PrivateRoute.jsx";

// Roles centralizados (estandarizados en mayúsculas, sin tildes ni espacios dobles)
export const ROLES = {
    SUPERADMIN: "SUPERADMIN",
    ADMINISTRADOR: "ADMINISTRADOR",
    COORD_MEDICO: "COORD_MEDICO",
    MEDICO: "MEDICO",
    COORD_ADMISION: "COORD_ADMISION",
    PROFESIONAL_SALUD: "PROFESIONAL_SALUD",
};

function App() {
    return (
        <BrowserRouter>
            <Routes>

                {/* ==================== RUTAS PÚBLICAS ==================== */}
                <Route path="/" element={<Login />} />
                {/* Alias de login → redirige al panel admin */}
                <Route path="/admin" element={<Navigate to="/admin-panel" replace />} />
                <Route path="/registro" element={<Registro />} />
                <Route path="/forgot-password" element={<ForgotPassword />} />
                <Route path="/reset-password" element={<ResetPassword />} />
                <Route path="/test-ping" element={<TestPing />} />

                {/* ==================== RUTAS PRIVADAS ==================== */}

                {/* Panel Admin */}
                <Route
                    path="/admin-panel"
                    element={
                        <PrivateRoute allowedRoles={[ROLES.SUPERADMIN, ROLES.ADMINISTRADOR]}>
                            <Admin />
                        </PrivateRoute>
                    }
                />

                {/* Coordinador de Especialidades */}
                <Route
                    path="/coordinador-especialidades"
                    element={
                        <PrivateRoute allowedRoles={[ROLES.COORD_MEDICO]}>
                            <CoordinadorEspecialidades />
                        </PrivateRoute>
                    }
                />

                {/* Portal Médico */}
                <Route
                    path="/portal-medico"
                    element={
                        <PrivateRoute allowedRoles={[ROLES.MEDICO]}>
                            <PortalMedico />
                        </PrivateRoute>
                    }
                />

                {/* Portal Profesional de Salud */}
                <Route
                    path="/portal-profesional-salud"
                    element={
                        <PrivateRoute allowedRoles={[ROLES.PROFESIONAL_SALUD]}>
                            <PortalProfesionalSalud />
                        </PrivateRoute>
                    }
                />

                {/* Gestión Territorial */}
                <Route
                    path="/gestion-territorial"
                    element={
                        <PrivateRoute allowedRoles={[ROLES.COORD_ADMISION, ROLES.ADMINISTRADOR]}>
                            <GestionTerritorial />
                        </PrivateRoute>
                    }
                />

                {/* ==================== RUTA 404 ==================== */}
                <Route path="*" element={<NotFound />} />

            </Routes>
        </BrowserRouter>
    );
}

export default App;
