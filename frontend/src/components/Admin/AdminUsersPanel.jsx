import { useState, useEffect } from "react";
import Sidebar from "./Sidebar.jsx";
import Header from "./Header";
import Stats from "./Stats";
import UserTable from "./UserTable";
import UserModal from "./UserModal";

export default function AdminUsersPanel() {
    const [users, setUsers] = useState([]);
    const [especialidades, setEspecialidades] = useState([]);
    const [roles, setRoles] = useState([]);
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [showUserModal, setShowUserModal] = useState(false);

    // 🔹 Cargar usuarios
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const res = await fetch("http://localhost:8080/api/profesionales");
                if (!res.ok) throw new Error("Error al obtener usuarios");
                const data = await res.json();

                const mappedUsers = Array.isArray(data)
                    ? data.map(u => ({
                        id: u.id,
                        nombres: u.nombres || "-",
                        apellidos: u.apellidos || "-",
                        correoUsuario: u.correoUsuario || "-",
                        roles: Array.isArray(u.roles)
                            ? u.roles.map(r => r.nombre?.toUpperCase() || "-")
                            : [],
                        activo: u.activoUsuario ?? true,
                        especialidad: u.especialidad ?? "No asignada",
                        tipoDocumento: u.tipoDocumento ?? "-",
                        numeroDocumento: u.numeroDocumento ?? "-",
                        telefono: u.telefono ?? "-",
                        ipressId: u.ipressId ?? null,
                        updatedAt: u.updatedAt ?? null
                    }))
                    : [];
                setUsers(mappedUsers);
            } catch (err) {
                console.error("Error al cargar usuarios:", err);
                setUsers([]);
            }
        };
        fetchUsers();
    }, []);

    // 🔹 Cargar especialidades
    useEffect(() => {
        const fetchEspecialidades = async () => {
            try {
                const res = await fetch("http://localhost:8080/api/especialidades");
                if (!res.ok) throw new Error("Error al obtener especialidades");
                const data = await res.json();
                setEspecialidades(Array.isArray(data) ? data : []);
            } catch (err) {
                console.error("Error al cargar especialidades:", err);
                setEspecialidades([]);
            }
        };
        fetchEspecialidades();
    }, []);

    // 🔹 Cargar roles únicos desde usuarios
    useEffect(() => {
        const rolesSet = new Set();
        users.forEach(u => {
            if (Array.isArray(u.roles)) u.roles.forEach(r => rolesSet.add(r));
        });
        setRoles(Array.from(rolesSet));
    }, [users]);

    const handleUserClick = (user) => {
        setSelectedUser(user);
        setShowUserModal(true);
    };

    const handleEditUser = (updatedUser) => {
        setUsers(prev => prev.map(u => u.id === updatedUser.id ? updatedUser : u));
    };

    return (
        <div className="min-h-screen bg-gray-50 flex">
            <Sidebar open={sidebarOpen} setOpen={setSidebarOpen} />
            <div className="flex-1 flex flex-col overflow-hidden">
                <Header
                    title="Gestión de Usuarios"
                    onMenuClick={() => setSidebarOpen(true)}
                    onInviteUser={() => { setSelectedUser(null); setShowUserModal(true); }}
                />
                <main className="flex-1 overflow-y-auto p-6 space-y-6">
                    <Stats users={users} especialidades={especialidades} />
                    <UserTable
                        users={users}
                        onEditUser={handleUserClick}
                    />
                </main>
            </div>

            {showUserModal && (
                <UserModal
                    isOpen={showUserModal}
                    onClose={() => setShowUserModal(false)}
                    userData={selectedUser}
                    onSave={handleEditUser}
                    roles={roles}
                    sitiosOptions={["Ipress 1", "Ipress 2", "Ipress 3"]}
                />
            )}
        </div>
    );
}
