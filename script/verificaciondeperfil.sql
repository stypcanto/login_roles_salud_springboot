-- Ver usuarios y roles
SELECT u.correo, r.nombre AS rol
FROM usuarios u
         JOIN usuario_roles ur ON u.id = ur.usuario_id
         JOIN roles r ON ur.rol_id = r.id;

-- Ver perfil de profesionales
SELECT p.nombres, p.apellidos, p.documento, p.telefono, u.correo
FROM profesionales p
         JOIN usuarios u ON p.usuario_id = u.id;
