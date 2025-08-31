-- ===========================
-- 7️⃣ TABLA ROL_PERMISOS
-- ===========================
-- Ejemplo: SUPERADMIN tiene todos los permisos
INSERT INTO rol_permisos (rol_id, permiso_id)
SELECT 1, id FROM permisos;