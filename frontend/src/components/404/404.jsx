const _404 = () => {
    return (
        <div className="flex flex-col items-center justify-center h-screen text-center bg-gray-100 dark:bg-gray-900">
            <h1 className="text-6xl font-bold text-blue-600 dark:text-blue-400">404</h1>
            <h2 className="mt-4 text-2xl font-semibold text-gray-800 dark:text-gray-200">
                Página no encontrada
            </h2>
            <p className="mt-2 text-gray-600 dark:text-gray-400">
                Lo sentimos, la página que buscas no existe.
            </p>
            <a
                href="/"
                className="px-6 py-3 mt-6 font-semibold text-white transition bg-blue-600 rounded-lg shadow-md hover:bg-blue-700"
            >
                Volver al inicio
            </a>
        </div>
    );
};

export default _404;
