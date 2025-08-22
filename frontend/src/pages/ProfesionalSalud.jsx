import { useEffect, useState } from "react";
import api from "../config/api";
import Footer_azul from "../components/Footer/Footer_azul";
import NavTransversal from "../components/Nav/Nav_intro";
import NavMenu from "../components/Menu/Menu";
import PortalMedico from "./PortalMedico";
import GestionTerritorial from "./GestionTerritorial";

const RolesPortal = () => {
    const [selectedSection, setSelectedSection] = useState(null);
    const [userName, setUserName] = useState("Usuario");

    useEffect(() => {
        console.log("📌 Ejecutando fetchUserData()...");

        const fetchUserData = async () => {
            const token = localStorage.getItem("token");

            if (!token) {
                console.error("❌ No hay token en localStorage. Debes iniciar sesión.");
                return;
            }

            console.log("🔍 Token enviado:", token);

            try {
                const response = await api.get("/portaladmin", {
                    headers: { Authorization: `Bearer ${token}` },
                });

                console.log("🔄 Estado de la respuesta:", response.status);

                const data = response.data;
                console.log("✅ Respuesta completa de la API:", JSON.stringify(data, null, 2));

                if (data.success && data.user) {
                    const nombre = data.user.nombres?.trim() || "Usuario";
                    console.log("📌 Nombre extraído:", nombre);
                    setUserName(nombre);
                } else {
                    console.warn("⚠️ No se pudo obtener el nombre del usuario.");
                }
            } catch (error) {
                console.error("⚠️ Error al obtener el usuario:", error.message);

                if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                    console.warn("🚨 Token inválido. Cerrando sesión...");
                    localStorage.removeItem("token");
                    window.location.href = "/login";
                }
            }
        };

        fetchUserData();
    }, []);

    return (
        <div className="flex flex-col min-h-screen overflow-hidden">
            {/* ✅ Barra de navegación superior */}
            <NavMenu
                onSelectSection={(section) => {
                    if (section === "PortalMedico") {
                        setSelectedSection(<PortalMedico />);
                    } else if (section === "GestionTerritorial") {
                        setSelectedSection(<GestionTerritorial />);
                    } else {
                        setSelectedSection(null);
                    }
                }}
            />

            <div className="flex flex-col flex-1 h-full lg:flex-row">
                {/* ✅ Contenido principal */}
                <div className="relative flex items-center justify-center flex-1">
                    {!selectedSection ? (
                        <>
                            {/* Imagen de fondo */}
                            <div
                                className="absolute inset-0 w-full h-full bg-center bg-cover"
                                style={{ backgroundImage: "url('/images/CENATEANGULAR.png')" }}
                            ></div>

                            {/* Capa oscura */}
                            <div className="absolute inset-0 bg-blue-100/80"></div>

                            {/* Contenido central */}
                            <div className="relative z-10 flex flex-col items-center justify-center px-4 text-center">
                                <h1 className="mt-20 md:mt-14 lg:mt-0 text-2xl md:text-4xl font-bold text-[#1d4f8a] drop-shadow-lg max-w-[95%] md:max-w-[70%]">
                                    ¡Bienvenido,{" "}
                                    <span className="text-[#ff8c00]">{userName}</span> al Portal de CENATE!
                                </h1>
                                <p className="mt-2 text-sm md:text-lg text-black drop-shadow max-w-[85%] md:max-w-[70%]">
                                    Accede a la información y herramientas que necesitas. Por favor,
                                    selecciona una opción del menú para comenzar.
                                </p>
                            </div>
                        </>
                    ) : (
                        selectedSection
                    )}
                </div>

                {/* ✅ NavTransversal en escritorio */}
                <div className="flex-col flex-shrink-0 hidden shadow-lg lg:flex w-72">
                    <NavTransversal />
                </div>
            </div>

            {/* ✅ NavTransversal para móviles */}
            <div className="w-full lg:hidden">
                <NavTransversal />
            </div>

            {/* ✅ Footer */}
            <Footer_azul />
        </div>
    );
};

export default RolesPortal;
