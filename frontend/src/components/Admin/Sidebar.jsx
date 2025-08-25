import { Users, Shield, Settings, LogOut, X } from "lucide-react";

export default function Sidebar({ sidebarOpen, setSidebarOpen, handleNavigation }) {
    const SidebarLink = ({ icon: Icon, text, active = false, onClick }) => (
        <button
            onClick={onClick}
            className={`w-full flex items-center px-4 py-3 text-left rounded-lg transition-colors ${
                active
                    ? 'bg-blue-600 text-white'
                    : 'text-gray-300 hover:bg-gray-700 hover:text-white'
            }`}
        >
            <Icon className="w-5 h-5 mr-3" />
            <span>{text}</span>
        </button>
    );

    return (
        <div className={`fixed inset-y-0 left-0 z-50 w-64 bg-gray-800 transform transition-transform duration-200 ease-in-out lg:translate-x-0 lg:static lg:inset-0 ${
            sidebarOpen ? 'translate-x-0' : '-translate-x-full'
        }`}>
            <div className="flex items-center justify-between h-16 px-4 bg-gray-900">
                <h1 className="text-xl font-bold text-white">Admin Panel</h1>
                <button
                    onClick={() => setSidebarOpen(false)}
                    className="lg:hidden text-gray-400 hover:text-white"
                >
                    <X className="w-6 h-6" />
                </button>
            </div>

            <nav className="mt-8 px-4 space-y-2">
                <SidebarLink icon={Users} text="Usuarios" active={true} />
                <SidebarLink icon={Shield} text="Roles" onClick={() => handleNavigation("/roles")} />
                <SidebarLink icon={Settings} text="Configuración" />
            </nav>

            <div className="absolute bottom-4 left-4 right-4">
                <button className="w-full flex items-center px-4 py-3 text-gray-300 hover:bg-gray-700 hover:text-white rounded-lg transition-colors">
                    <LogOut className="w-5 h-5 mr-3" />
                    <span>Cerrar Sesión</span>
                </button>
            </div>
        </div>
    );
}
