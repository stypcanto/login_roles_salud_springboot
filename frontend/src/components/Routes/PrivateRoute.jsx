import { Navigate } from "react-router-dom";

export default function PrivateRoute({ children, allowedRoles = [] }) {
    const user = JSON.parse(localStorage.getItem("user"));

    if (!user) return <Navigate to="/" replace />;

    if (allowedRoles.length > 0 && !user.roles.some(r => allowedRoles.includes(r))) {
        return <Navigate to="/" replace />;
    }

    return children;
}
