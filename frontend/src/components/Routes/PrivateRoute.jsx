import { Navigate } from "react-router-dom";

const PrivateRoute = ({ children }) => {
    const token = localStorage.getItem("token"); // o usa tu contexto de auth

    if (!token) {
        return <Navigate to="/" replace />; // 👈 redirige al login si no hay token
    }

    return children; // 👈 si hay token, muestra el componente protegido
};

export default PrivateRoute;
