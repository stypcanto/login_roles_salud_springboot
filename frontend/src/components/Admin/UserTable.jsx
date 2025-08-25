import { useState, useEffect } from "react";
import UserModal from "./UserModal";
import { Badge } from "@/components/ui/Badge";

import { Button } from "@/components/ui/Button";
import { Card, CardContent } from "@/components/ui/Card";

export default function UserTable() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [selectedUser, setSelectedUser] = useState(null);

    // 🔹 Obtener datos del backend
    useEffect(() => {
        fetch("http://localhost:8080/api/profesionales")
            .then((res) => res.json())
            .then((data) => {
                setUsers(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error al cargar profesionales:", err);
                setLoading(false);
            });
    }, []);

    const handleEdit = (user) => setSelectedUser(user);
    const handleCloseModal = () => setSelectedUser(null);

    const handleSaveUser = (updatedUser) => {
        setUsers((prev) =>
            prev.map((u) => (u.id === updatedUser.id ? updatedUser : u))
        );
        setSelectedUser(null);
    };

    if (loading) return <p className="text-center text-gray-500">⏳ Cargando usuarios...</p>;

    return (
        <div className="p-6">
            <h2 className="text-3xl font-bold mb-6 text-gray-800">
                👥 Lista de Profesionales
            </h2>

            <Card className="shadow-lg rounded-2xl border border-gray-200">
                <CardContent className="overflow-x-auto p-0">
                    <table className="w-full text-sm text-left border-collapse">
                        <thead className="bg-gradient-to-r from-blue-500 to-indigo-600 text-white">
                        <tr>
                            <th className="p-3">Nombres</th>
                            <th className="p-3">Apellidos</th>
                            <th className="p-3">Documento</th>
                            <th className="p-3">Teléfono</th>
                            <th className="p-3">Correo</th>
                            <th className="p-3">Roles</th>
                            <th className="p-3 text-center">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        {users.map((user, idx) => (
                            <tr
                                key={user.id}
                                className={`${
                                    idx % 2 === 0 ? "bg-gray-50" : "bg-white"
                                } hover:bg-gray-100 transition-colors`}
                            >
                                <td className="p-3 font-medium text-gray-900">{user.nombres}</td>
                                <td className="p-3">{user.apellidos}</td>
                                <td className="p-3">
                                        <span className="font-semibold text-gray-700">
                                            {user.tipoDocumento}
                                        </span>{" "}
                                    {user.numeroDocumento}
                                </td>
                                <td className="p-3">{user.telefono}</td>
                                <td className="p-3 text-blue-600">{user.correoUsuario}</td>
                                <td className="p-3">
                                    {Array.isArray(user.roles) && user.roles.length > 0 ? (
                                        user.roles.map((role, i) => (
                                            <Badge key={i} variant="secondary" className="mr-1">
                                                {typeof role === "string" ? role : role.nombre}
                                            </Badge>
                                        ))
                                    ) : (
                                        <Badge variant="outline">Sin rol</Badge>
                                    )}
                                </td>
                                <td className="p-3 text-center">
                                    <Button
                                        size="sm"
                                        onClick={() => handleEdit(user)}
                                        className="bg-blue-500 hover:bg-blue-600 text-white rounded-lg"
                                    >
                                        ✏️ Editar
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </CardContent>
            </Card>

            {/* Modal */}
            {selectedUser && (
                <UserModal
                    isOpen={!!selectedUser}
                    userData={selectedUser}
                    onClose={handleCloseModal}
                    onSave={handleSaveUser}
                />
            )}
        </div>
    );
}
