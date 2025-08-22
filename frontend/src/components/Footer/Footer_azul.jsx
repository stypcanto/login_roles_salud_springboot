import React from "react";

const Footer_azul = () => {
    return (
        <footer className="bg-[#0a5ba9] text-white py-2 fixed bottom-0 w-full">

            <div className="container mx-auto text-center">
                {/*
        <div className="flex justify-center mb-4 space-x-6">
          <a href="/" className="transition-colors duration-300 hover:text-blue-400">Inicio</a>
          <a href="/about" className="transition-colors duration-300 hover:text-blue-400">Acerca de</a>
          <a href="/contact" className="transition-colors duration-300 hover:text-blue-400">Contacto</a>
        </div>
        */}
                <div className="py-0 text-xs text-center text-white border-gray-300">
                    <p className="mb-1">
                        &copy; {new Date().getFullYear()}{" "}
                        <span className="font-semibold">CENATE - ESSALUD</span>. Todos los derechos reservados.
                    </p>
                    <p className="mb-1">
                        Diseñado y desarrollado por{" "}
                        <span className="font-semibold text-white">Equipo de Gestión TI</span>.
                    </p>
                    <p className="text-white">Versión 1.0</p>
                </div>
            </div>
        </footer>
    );
};

export default Footer_azul;
