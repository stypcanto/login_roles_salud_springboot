import { useState } from 'react';

export default function TestPing() {
  const [response, setResponse] = useState('');

  const testPing = async () => {
    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}/auth/ping`);
      const text = await res.text();
      setResponse(text);
    } catch (error) {
      setResponse('Error: ' + error.message);
    }
  };

  return (
    <div>
      <button onClick={testPing}>Probar conexión backend</button>
      <p>Respuesta: {response}</p>
    </div>
  );
}
