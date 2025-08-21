import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./Login";
import Dashboard from "./Dashboard";
import Registro from "./Registro";
import TestPing from './TestPing';
import ForgotPassword from "./ForgotPassword";
import ResetPassword from "./ResetPassword";


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
      </Routes>
    </BrowserRouter>
  );
}

export default App;
