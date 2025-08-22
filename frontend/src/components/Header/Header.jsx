const Header_template = ({ children }) => {
    return (
        <div className="bg-[#0a5ba9] text-white w-full h-auto border-b-2">
            <div className="flex flex-row items-center justify-between p-1 mx-auto max-w-7xl sm:flex-row">
                <div className="mb-2 text-center sm:mb-0">
                    <img
                        src="/images//LOGOESSALUD.png"
                        alt="Centro Nacional de Telemedicina - CENATE"
                        className="h-auto mx-auto ml-2 sm:w-40 w-22"
                    />
                </div>
                {/* Aquí se renderiza el botón de cierre de sesión o cualquier otro contenido dinámico */}
                {children && <div className="flex items-center mt-2 sm:mt-0">{children}</div>}
            </div>
        </div>
    );
};

export default Header_template;
