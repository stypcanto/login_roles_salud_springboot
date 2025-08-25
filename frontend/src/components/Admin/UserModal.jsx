import { useEffect, useMemo, useState } from "react";
import { X } from "lucide-react";

/**
 * Modal de edición/creación de Profesional
 * - Soporta creación y edición
 * - Secciones plegables con <details>
 * - Profesión y especialidad cargadas dinámicamente
 * - onSave envía el DTO correcto a tu backend
 */
export default function UserModal({
                                      isOpen,
                                      onClose,
                                      userData = null,
                                      onSave,
                                      roles = [],
                                      sitiosOptions = [],
                                  }) {
    // ---------- estado base del formulario ----------
    const [form, setForm] = useState({
        id: null,
        nombres: "",
        apellidos: "",
        email: "",
        rol: "",
        activo: true,
        telefono: "",
        colegiatura: "",
        rne: "",
        tipoDocumento: "",
        numeroDocumento: "",
        profesionId: "",
        especialidadId: "",
        sitios: [],
    });

    // ---------- datos dinámicos desde backend ----------
    const [especialidades, setEspecialidades] = useState([]);
    const [profesiones, setProfesiones] = useState([]);

    // Iniciales del avatar
    const initials = useMemo(() => {
        const n = (form.nombres || "").trim();
        const a = (form.apellidos || "").trim();
        const i1 = n ? n[0].toUpperCase() : "?";
        const i2 = a ? a[0].toUpperCase() : "";
        return `${i1}${i2}`;
    }, [form.nombres, form.apellidos]);

    // ---------- cargar datos del usuario ----------
    useEffect(() => {
        if (userData) {
            setForm({
                id: userData.id ?? null,
                nombres: userData.nombres ?? "",
                apellidos: userData.apellidos ?? "",
                email: userData.correoUsuario ?? userData.email ?? "",
                rol:
                    Array.isArray(userData.roles) && userData.roles.length > 0
                        ? userData.roles[0]
                        : userData.rol ?? "",
                activo: userData.activoUsuario ?? userData.activo ?? true,
                telefono: userData.telefono ?? "",
                colegiatura: userData.colegiatura ?? "",
                rne: userData.rne ?? "",
                tipoDocumento: userData.tipoDocumento ?? "",
                numeroDocumento: userData.numeroDocumento ?? "",
                profesionId: userData.profesionId ?? "",
                especialidadId: userData.especialidadId ?? "",
                sitios: userData.sitios ?? [],
            });
        } else {
            setForm({
                id: null,
                nombres: "",
                apellidos: "",
                email: "",
                rol: "",
                activo: true,
                telefono: "",
                colegiatura: "",
                rne: "",
                tipoDocumento: "",
                numeroDocumento: "",
                profesionId: "",
                especialidadId: "",
                sitios: [],
            });
        }
    }, [userData]);

    // ---------- cargar profesiones y especialidades ----------
    useEffect(() => {
        if (!isOpen) return;
        (async () => {
            try {
                const [espRes, profRes] = await Promise.all([
                    fetch("http://localhost:8080/api/especialidades"),
                    fetch("http://localhost:8080/api/profesiones"),
                ]);
                const [espJson, profJson] = await Promise.all([
                    espRes.json(),
                    profRes.json(),
                ]);
                setEspecialidades(Array.isArray(espJson) ? espJson : []);
                setProfesiones(Array.isArray(profJson) ? profJson : []);
            } catch (e) {
                console.error("Error cargando profesiones/especialidades:", e);
            }
        })();
    }, [isOpen]);

    // Filtrar especialidades por profesión
    const especialidadesFiltradas = useMemo(() => {
        if (!form.profesionId) return especialidades;
        const pid = Number(form.profesionId);
        return especialidades.filter((e) => Number(e.profesionId) === pid);
    }, [especialidades, form.profesionId]);

    // ---------- handlers ----------
    const handleChange = (e) => {
        const { name, value, type, checked, options } = e.target;
        if (type === "checkbox") {
            setForm((prev) => ({ ...prev, [name]: checked }));
        } else if (type === "select-multiple") {
            const selected = Array.from(options)
                .filter((o) => o.selected)
                .map((o) => o.value);
            setForm((prev) => ({ ...prev, [name]: selected }));
        } else {
            setForm((prev) => ({ ...prev, [name]: value }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const payload = {
            nombres: form.nombres,
            apellidos: form.apellidos,
            tipoDocumento: form.tipoDocumento || null,
            numeroDocumento: form.numeroDocumento || null,
            rne: form.rne || null,
            colegiatura: form.colegiatura || null,
            telefono: form.telefono || null,
            profesionId: form.profesionId ? Number(form.profesionId) : null,
            especialidadId: form.especialidadId
                ? Number(form.especialidadId)
                : null,
            correoUsuario: form.email || null,
        };
        await onSave({ id: form.id, ...payload });
        onClose?.();
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-[100] flex items-center justify-center bg-black/50 p-4">
            <div className="bg-white rounded-2xl shadow-xl w-full max-w-5xl relative overflow-hidden">
                {/* Header */}
                <div className="flex items-center justify-between px-6 py-4 border-b">
                    <div className="flex items-center gap-4">
                        <div className="w-12 h-12 rounded-full bg-blue-100 text-blue-700 flex items-center justify-center font-semibold">
                            {initials}
                        </div>
                        <div>
                            <h3 className="text-lg font-semibold">
                                {form.id ? "Editar profesional" : "Nuevo profesional"}
                            </h3>
                            <p className="text-sm text-gray-500">
                                {form.email || "Sin correo"}
                            </p>
                        </div>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 rounded-lg hover:bg-gray-100 text-gray-500"
                        aria-label="Cerrar modal"
                    >
                        <X className="w-5 h-5" />
                    </button>
                </div>

                {/* Contenido */}
                <form onSubmit={handleSubmit} className="max-h-[75vh] overflow-y-auto">
                    <div className="p-6 space-y-6">
                        {/* Sección: Datos de cuenta */}
                        <details open className="group rounded-xl border">
                            <summary className="cursor-pointer list-none px-4 py-3 flex items-center justify-between">
                                <span className="font-medium">Datos de cuenta</span>
                                <span className="text-sm text-gray-500 group-open:rotate-180 transition-transform">
                  ▾
                </span>
                            </summary>
                            <div className="px-4 pb-4 grid grid-cols-1 sm:grid-cols-3 gap-4">
                                <div>
                                    <label className="block text-sm mb-1">Nombres</label>
                                    <input
                                        name="nombres"
                                        value={form.nombres}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm mb-1">Apellidos</label>
                                    <input
                                        name="apellidos"
                                        value={form.apellidos}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm mb-1">Correo</label>
                                    <input
                                        type="email"
                                        name="email"
                                        value={form.email}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm mb-1">Rol</label>
                                    <select
                                        name="rol"
                                        value={form.rol}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                    >
                                        <option value="">Seleccione rol</option>
                                        {roles.map((r) => (
                                            <option key={r} value={r}>
                                                {r}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div className="flex items-center gap-2 pt-6">
                                    <input
                                        id="activo"
                                        type="checkbox"
                                        name="activo"
                                        checked={!!form.activo}
                                        onChange={handleChange}
                                        className="h-4 w-4"
                                    />
                                    <label htmlFor="activo" className="text-sm">
                                        Activo
                                    </label>
                                </div>
                            </div>
                        </details>

                        {/* Sección: Identificación */}
                        <details open className="group rounded-xl border">
                            <summary className="cursor-pointer list-none px-4 py-3 flex items-center justify-between">
                                <span className="font-medium">Identificación</span>
                                <span className="text-sm text-gray-500 group-open:rotate-180 transition-transform">
                  ▾
                </span>
                            </summary>
                            <div className="px-4 pb-4 grid grid-cols-1 sm:grid-cols-3 gap-4">
                                <div>
                                    <label className="block text-sm mb-1">Tipo de documento</label>
                                    <select
                                        name="tipoDocumento"
                                        value={form.tipoDocumento}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                    >
                                        <option value="">Seleccione</option>
                                        <option value="DNI">DNI</option>
                                        <option value="C.Extranjería">C.Extranjería</option>
                                        <option value="Pasaporte">Pasaporte</option>
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm mb-1">N° documento</label>
                                    <input
                                        name="numeroDocumento"
                                        value={form.numeroDocumento}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm mb-1">Teléfono</label>
                                    <input
                                        name="telefono"
                                        value={form.telefono}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                    />
                                </div>
                            </div>
                        </details>

                        {/* Sección: Profesión y Especialidad */}
                        <details open className="group rounded-xl border">
                            <summary className="cursor-pointer list-none px-4 py-3 flex items-center justify-between">
                                <span className="font-medium">Profesión y especialidad</span>
                                <span className="text-sm text-gray-500 group-open:rotate-180 transition-transform">
                  ▾
                </span>
                            </summary>
                            <div className="px-4 pb-4 grid grid-cols-1 sm:grid-cols-3 gap-4">
                                <div>
                                    <label className="block text-sm mb-1">Profesión</label>
                                    <select
                                        name="profesionId"
                                        value={form.profesionId}
                                        onChange={(e) =>
                                            setForm((prev) => ({
                                                ...prev,
                                                profesionId: e.target.value,
                                                especialidadId: "",
                                            }))
                                        }
                                        className="w-full border rounded-lg px-3 py-2"
                                    >
                                        <option value="">Seleccione profesión</option>
                                        {profesiones.map((p) => (
                                            <option key={p.id} value={p.id}>
                                                {p.nombre}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm mb-1">Especialidad</label>
                                    <select
                                        name="especialidadId"
                                        value={form.especialidadId}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                        disabled={!especialidadesFiltradas.length}
                                    >
                                        <option value="">Seleccione especialidad</option>
                                        {especialidadesFiltradas.map((e) => (
                                            <option key={e.id} value={e.id}>
                                                {e.nombre}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm mb-1">RNE</label>
                                    <input
                                        name="rne"
                                        value={form.rne}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm mb-1">Colegiatura</label>
                                    <input
                                        name="colegiatura"
                                        value={form.colegiatura}
                                        onChange={handleChange}
                                        className="w-full border rounded-lg px-3 py-2"
                                    />
                                </div>
                            </div>
                        </details>

                        {/* Sección: Sitios */}
                        <details className="group rounded-xl border">
                            <summary className="cursor-pointer list-none px-4 py-3 flex items-center justify-between">
                                <span className="font-medium">Sitios</span>
                                <span className="text-sm text-gray-500 group-open:rotate-180 transition-transform">
                  ▾
                </span>
                            </summary>
                            <div className="px-4 pb-4">
                                <select
                                    name="sitios"
                                    multiple
                                    value={form.sitios}
                                    onChange={handleChange}
                                    className="w-full border rounded-lg px-3 py-2 h-32"
                                >
                                    {sitiosOptions.map((s) => (
                                        <option key={s} value={s}>
                                            {s}
                                        </option>
                                    ))}
                                </select>
                                <p className="mt-2 text-xs text-gray-500">
                                    Usa Ctrl (o Cmd en Mac) para seleccionar varios.
                                </p>
                            </div>
                        </details>
                    </div>

                    {/* Footer */}
                    <div className="px-6 py-4 border-t bg-gray-50 flex items-center justify-end gap-3">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-4 py-2 rounded-lg border hover:bg-gray-100"
                        >
                            Cancelar
                        </button>
                        <button
                            type="submit"
                            className="px-4 py-2 rounded-lg bg-blue-600 text-white hover:bg-blue-700"
                        >
                            Guardar
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
