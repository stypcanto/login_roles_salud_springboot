import { useState, useEffect } from "react";
import { Users, Shield, Settings } from "lucide-react";

// Componentes locales
import Sidebar from "./Sidebar.jsx";
import Header from "./Header";
import Stats from "./Stats";
import UserTable from "./UserTable";
import UserModal from "./UserModal";

// Roles disponibles (los puedes traer de tu backend también si quieres)
const roles = ["Superadmin", "Administrador", "Coordinador Medico", "Medico", "Coordinador Admision"];

export default function AdminUsersPanel() {
    const [users, setUsers] = useState([]);
    const [especialidades, setEspecialidades] = useState([]); // 👈 Guardamos especialidades del backend
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedRole, setSelectedRole] = useState("");
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [showUserModal, setShowUserModal] = useState(false);

    // 📌 Cargar usuarios
    useEffect(() => {
        fetch("http://localhost:8080/api/profesionales")
            .then(res => res.ok ? res.json() : Promise.reject("Error al obtener usuarios"))
            .then(data => {
                const mappedUsers = data.map(u => ({
                    id: u.id,
                    nombres: u.nombres,
                    apellidos: u.apellidos,
                    email: u.correoUsuario ?? "",
                    rol: u.roles && u.roles.length > 0 ? u.roles[0] : "Sin rol",
                    activo: u.activoUsuario,
                    especialidad: u.especialidad ?? "No asignada",
                    tipoDocumento: u.tipoDocumento ?? "",      // ✅ agregado
                    numeroDocumento: u.numeroDocumento ?? "",  // ✅ agregado
                    sitios: u.ipressId ? [`Ipress ${u.ipressId}`] : [],
                    ultimoAcceso: u.updatedAt?.split("T")[0] ?? ""
                }));
                setUsers(mappedUsers);
            })
            .catch(err => {
                console.error("Error al cargar usuarios:", err);
                setUsers([]);
            });
    }, []);

    // 📌 Cargar especialidades
    useEffect(() => {
        fetch("http://localhost:8080/api/especialidades")
            .then(res => res.ok ? res.json() : Promise.reject("Error al obtener especialidades"))
            .then(data => {
                setEspecialidades(data); // 👈 Guardamos todas las especialidades que vienen del backend
            })
            .catch(err => {
                console.error("Error al cargar especialidades:", err);
                setEspecialidades([]);
            });
    }, []);

    // 📌 Filtros de búsqueda y rol
    const filteredUsers = users.filter(user => {
        const fullName = `${user.nombres} ${user.apellidos}`.toLowerCase();
        const matchesSearch =
            fullName.includes(searchTerm.toLowerCase()) ||
            (user.email && user.email.toLowerCase().includes(searchTerm.toLowerCase()));
        const matchesRole = selectedRole === "" || user.rol === selectedRole;
        return matchesSearch && matchesRole;
    });

    // 📌 Editar usuario
    const handleEditUser = (updatedUser) => {
        setUsers(prev => prev.map(u => u.id === updatedUser.id ? updatedUser : u));
    };

    // 📌 Eliminar usuario
    const handleDeleteUser = (userId) => {
        if (window.confirm("¿Estás seguro de eliminar este usuario?")) {
            setUsers(prev => prev.filter(u => u.id !== userId));
        }
    };

    // 📌 Invitar usuario (abre modal vacío)
    const handleInviteUser = () => {
        setSelectedUser(null);
        setShowUserModal(true);
    };

    return (
        <div className="min-h-screen bg-gray-50 flex">
            {/* Sidebar */}
            <Sidebar open={sidebarOpen} setOpen={setSidebarOpen} />

            {/* Contenido principal */}
            <div className="flex-1 flex flex-col overflow-hidden">
                <Header
                    title="Gestión de Usuarios"
                    onMenuClick={() => setSidebarOpen(true)}
                    onInviteUser={handleInviteUser}
                />

                <main className="flex-1 overflow-y-auto p-6 space-y-6">
                    {/* 📊 Estadísticas */}
                    <Stats users={users} especialidades={especialidades} />

                    {/* 📋 Tabla de usuarios */}
                    <UserTable
                        users={filteredUsers}
                        searchTerm={searchTerm}
                        setSearchTerm={setSearchTerm}
                        selectedRole={selectedRole}
                        setSelectedRole={setSelectedRole}
                        roles={roles}
                        especialidades={especialidades} // 👈 Pasamos especialidades
                        handleUserClick={user => { setSelectedUser(user); setShowUserModal(true); }}
                        handleEditUser={handleEditUser}
                        handleDeleteUser={handleDeleteUser}
                    />
                </main>
            </div>

            {/* Modal de edición/invitación */}
            <UserModal
                isOpen={showUserModal}
                onClose={() => setShowUserModal(false)}
                userData={selectedUser}
                roles={roles}
                onSave={(form) => {
                    if (selectedUser) {
                        handleEditUser({ ...selectedUser, ...form });
                    } else {
                        setUsers(prev => [...prev, { id: Date.now(), ...form }]);
                    }
                }}
            />
        </div>
    );
}
