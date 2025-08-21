import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "../pages/Login.jsx";
import Dashboard from "../pages/Dashboard.jsx";
import Registro from "../pages/Registro.jsx";
import TestPing from '../pages/TestPing.jsx';
import ForgotPassword from "../pages/ForgotPassword.jsx";
import ResetPassword from "../pages/ResetPassword.jsx";
import NotFound from "../components/404/404.jsx";



function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/test-ping" element={<TestPing />} />
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/registro" element={<Registro />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />

        {/* ✅ Ruta 404 */}
        <Route path="*" element={<NotFound />} />


      </Routes>
    </BrowserRouter>
  );
}

export default App;
