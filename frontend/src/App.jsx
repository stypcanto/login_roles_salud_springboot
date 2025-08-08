import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./Login";
import Dashboard from "./Dashboard";
import Registro from "./Registro";
import TestPing from './TestPing';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/test-ping" element={<TestPing />} />
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/registro" element={<Registro />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
