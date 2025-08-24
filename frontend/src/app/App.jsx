import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "../pages/Login.jsx";
import CoordinadorEspecialidades from "../pages/CoordinadorEspecialidades.jsx";
import Registro from "../pages/Registro.jsx";
import TestPing from "../pages/TestPing.jsx";
import ForgotPassword from "../pages/ForgotPassword.jsx";
import ResetPassword from "../pages/ResetPassword.jsx";
import NotFound from "../components/404/404.jsx";
import Admin from "../pages/Admin.jsx";
import PortalMedico from "../pages/PortalMedico.jsx";
import GestionTerritorial from "../pages/GestionTerritorial.jsx";
import PrivateRoute from "../components/Routes/PrivateRoute.jsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/test-ping" element={<TestPing />} />
                <Route path="/" element={<Login />} /> // Login
                <Route path="/admin" element={<Login />} />
                <Route path="/registro" element={<Registro />} />
                <Route path="/forgot-password" element={<ForgotPassword />} />
                <Route path="/reset-password" element={<ResetPassword />} />

                {/* Rutas según roles */}
                <Route
                    path="/admin-panel"  // Panel Admin
                    element={
                        <PrivateRoute>
                            <Admin />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/CoordinadorEspecialidades"
                    element={
                        <PrivateRoute>
                            <CoordinadorEspecialidades />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/PortalMedico"
                    element={
                        <PrivateRoute>
                            <PortalMedico />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/GestionTerritorial"
                    element={
                        <PrivateRoute>
                            <GestionTerritorial />
                        </PrivateRoute>
                    }
                />

                {/* Ruta 404 */}
                <Route path="*" element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
