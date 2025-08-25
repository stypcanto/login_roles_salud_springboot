import { UserPlus, Bell, Menu } from "lucide-react";

export default function Header({ setSidebarOpen, handleInviteUser }) {
    return (
        <header className="bg-white shadow-sm border-b border-gray-200">
            <div className="flex items-center justify-between px-6 py-4">
                <div className="flex items-center">
                    <button
                        onClick={() => setSidebarOpen(true)}
                        className="lg:hidden text-gray-600 hover:text-gray-900 mr-4"
                    >
                        <Menu className="w-6 h-6" />
                    </button>
                    <h2 className="text-2xl font-bold text-gray-900">Gestión de Usuarios</h2>
                </div>

                <div className="flex items-center space-x-4">
                    <button
                        onClick={handleInviteUser}
                        className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                    >
                        <UserPlus className="w-4 h-4 mr-2" />
                        Invitar Usuario
                    </button>

                    <button className="p-2 text-gray-600 hover:text-gray-900 relative">
                        <Bell className="w-6 h-6" />
                        <span className="absolute top-0 right-0 w-2 h-2 bg-red-500 rounded-full"></span>
                    </button>
                </div>
            </div>
        </header>
    );
}
