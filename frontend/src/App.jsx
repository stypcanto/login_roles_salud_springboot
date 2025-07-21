import { useEffect, useState } from 'react';

function App() {
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/') // o /api/ si agregas mapeo en Spring
      .then((res) => res.text())
      .then((data) => setMessage(data));
  }, []);

  return (
    <div className="p-8 text-center">
      <h1 className="text-2xl font-bold text-blue-600">Sistema de Citas Médicas</h1>
      <p className="mt-4">Mensaje desde Spring Boot:</p>
      <p className="font-mono text-green-700">{message}</p>
    </div>
  );
}

export default App;
