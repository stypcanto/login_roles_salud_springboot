import React from "react";
import menuIcon from "/images/home.png"; // Asegúrate de que esta imagen sea del ícono que deseas usar

function Nav_intro() {
    return (
        <div
            className="relative z-40 w-full text-black transition-all duration-300 bg-white border-r border-gray-300 sm:w-48"
            style={ { minHeight: 'auto' } } // Ajuste automático de altura en móviles
        >
            {/* Menú */ }
            <div className="mt-4">
                <ul className="space-y-4">
                    {/* Información General con fondo gris */ }
                    <li className="px-4 py-3 transition-all duration-300 bg-gray-300 hover:bg-gray-200 group">
                        <a
                            href="/navmenu"
                            className="flex items-center font-normal text-black transition-all group-hover:text-gray-700"
                        >
                            {/* Ícono Home */ }
                            <img
                                src={ menuIcon }
                                alt="Menu Icon"
                                className="w-8 h-8 cursor-pointer" />
                            {/* Nombre de la página - se oculta en móviles */ }
                            <span className="hidden ml-4 text-sm sm:block">Página Principal</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>

    );
}

export default Nav_intro;
