import { Users, Shield, Settings, Briefcase } from "lucide-react";

export default function Stats({ users, especialidades }) {
    return (
        <div className="mt-6 grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
            {/* Total usuarios */}
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
                <div className="flex items-center">
                    <Users className="w-8 h-8 text-blue-600" />
                    <div className="ml-4">
                        <p className="text-sm font-medium text-gray-500">Total Usuarios</p>
                        <p className="text-2xl font-semibold text-gray-900">{users.length}</p>
                    </div>
                </div>
            </div>

            {/* Usuarios activos */}
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
                <div className="flex items-center">
                    <Shield className="w-8 h-8 text-green-600" />
                    <div className="ml-4">
                        <p className="text-sm font-medium text-gray-500">Usuarios Activos</p>
                        <p className="text-2xl font-semibold text-gray-900">
                            {users.filter(u => u.activo).length}
                        </p>
                    </div>
                </div>
            </div>

            {/* Roles únicos */}
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
                <div className="flex items-center">
                    <Settings className="w-8 h-8 text-orange-600" />
                    <div className="ml-4">
                        <p className="text-sm font-medium text-gray-500">Roles Únicos</p>
                        <p className="text-2xl font-semibold text-gray-900">
                            {new Set(users.map(u => u.rol)).size}
                        </p>
                    </div>
                </div>
            </div>

            {/* Especialidades únicas */}
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
                <div className="flex items-center">
                    <Briefcase className="w-8 h-8 text-purple-600" />
                    <div className="ml-4">
                        <p className="text-sm font-medium text-gray-500">Especialidades</p>
                        <p className="text-2xl font-semibold text-gray-900">
                            {especialidades.length}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}
