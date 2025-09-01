import { useEffect, useState } from "react";
import api from "../config/api";
import Footer_azul from "../components/Footer/Footer_azul";
import NavTransversal from "../components/Nav/Nav_intro";
import NavMenu from "../components/Menu/Menu";
import PortalMedico from "./PortalMedico";
import GestionTerritorial from "./GestionTerritorial";

const PortalProfesionalSalud = () => {
    const [selectedSection, setSelectedSection] = useState(null);
    const [userName, setUserName] = useState("Usuario");
    const [showIframe, setShowIframe] = useState(false);

    useEffect(() => {
        console.log("📌 Ejecutando fetchUserData()...");

        const fetchUserData = async () => {
            const token = localStorage.getItem("token");

            if (!token) {
                console.error("❌ No hay token en localStorage. Debes iniciar sesión.");
                return;
            }

            try {
                // 🔑 Cambié el endpoint a uno permitido para PROFESIONAL_SALUD
                const response = await api.get("/api/profesionales/me", {
                    headers: { Authorization: `Bearer ${token}` },
                });

                console.log("🔄 Estado de la respuesta:", response.status);

                const data = response.data;
                console.log("✅ Respuesta completa de la API:", JSON.stringify(data, null, 2));

                if (data.success && data.user) {
                    const nombre = data.user.nombres?.trim() || "Usuario";
                    setUserName(nombre);
                } else {
                    console.warn("⚠️ No se pudo obtener el nombre del usuario.");
                }
            } catch (error) {
                console.error("⚠️ Error al obtener el usuario:", error.message);

                if (error.response && (error.response.status === 401)) {
                    console.warn("🚨 Token inválido. Cerrando sesión...");
                    localStorage.removeItem("token");
                    window.location.href = "/admin";
                } else if (error.response && error.response.status === 403) {
                    console.warn("🚨 Acceso prohibido para este rol.");
                    setSelectedSection(null); // solo oculta secciones restringidas
                }
            }
        };

        fetchUserData();
    }, []);

    return (
        <div className="flex flex-col min-h-screen overflow-hidden">
            {/* Navbar superior */}
            <NavMenu
                onSelectSection={(section) => {
                    if (section === "PortalMedico") setSelectedSection(<PortalMedico />);
                    else if (section === "GestionTerritorial") setSelectedSection(<GestionTerritorial />);
                    else setSelectedSection(null);
                }}
            />

            <div className="flex flex-col flex-1 h-full lg:flex-row">
                {/* Contenido principal */}
                <div className="relative flex items-center justify-center flex-1">
                    {!selectedSection ? (
                        <>
                            <div
                                className="absolute inset-0 w-full h-full bg-center bg-cover"
                                style={{ backgroundImage: "url('/images/CENATEANGULAR.png')" }}
                            ></div>
                            <div className="absolute inset-0 bg-blue-100/80"></div>

                            <div className="relative z-10 flex flex-col items-center justify-center px-4 text-center">
                                <h1 className="mt-20 md:mt-14 lg:mt-0 text-2xl md:text-4xl font-bold text-[#1d4f8a] drop-shadow-lg max-w-[95%] md:max-w-[70%]">
                                    ¡Bienvenido, <span className="text-[#ff8c00]">{userName}</span> al Portal de CENATE!
                                </h1>
                                <p className="mt-2 text-sm md:text-lg text-black drop-shadow max-w-[85%] md:max-w-[70%]">
                                    Accede a la información y herramientas que necesitas. Por favor, selecciona una opción del menú para comenzar.
                                </p>

                                <button
                                    onClick={() => setShowIframe(true)}
                                    className="mt-6 px-6 py-3 bg-[#1d4f8a] text-white font-bold rounded-2xl shadow-lg hover:bg-[#163a66] transition"
                                >
                                    Ver Reporte Power BI
                                </button>
                            </div>
                        </>
                    ) : (
                        selectedSection
                    )}
                </div>

                {/* NavTransversal escritorio */}
                <div className="flex-col flex-shrink-0 hidden shadow-lg lg:flex w-72">
                    <NavTransversal />
                </div>
            </div>

            {/* NavTransversal móvil */}
            <div className="w-full lg:hidden">
                <NavTransversal />
            </div>

            {/* Footer */}
            <Footer_azul />

            {/* Modal con iframe */}
            {showIframe && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-70">
                    <div className="relative bg-white rounded-2xl shadow-lg w-[90%] h-[85%] flex flex-col">
                        <button
                            onClick={() => setShowIframe(false)}
                            className="absolute top-3 right-3 bg-red-500 text-white px-3 py-1 rounded-lg hover:bg-red-700"
                        >
                            ✖
                        </button>
                        <iframe
                            title="BI_Crónicos"
                            src="https://app.powerbi.com/view?r=eyJrIjoiZGEyZTcxYTgtOGMxZS00Y2NhLWFiZGYtYzJlM2U0MDU1NGYxIiwidCI6IjM0ZjMyNDE5LTFjMDUtNDc1Ni04OTZlLTQ1ZDYzMzcyNjU5YiIsImMiOjR9"
                            className="w-full h-full rounded-b-2xl"
                            frameBorder="0"
                            allowFullScreen={true}
                        ></iframe>
                    </div>
                </div>
            )}
        </div>
    );
};

export default PortalProfesionalSalud;
