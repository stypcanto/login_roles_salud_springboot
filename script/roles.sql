-- ===========================
-- 1️⃣ TABLA ROLES
-- ===========================
INSERT INTO roles (id, nombre, descripcion) VALUES
                                                (1, 'SUPERADMIN', 'Super administrador con acceso total'),
                                                (2, 'ADMINISTRADOR', 'Administrador general del sistema'),
                                                (3, 'COORDINADOR_MEDICO', 'Coordina actividades médicas'),
                                                (4, 'MEDICO', 'Profesional de la salud: médico'),
                                                (5, 'COORDINADOR_ADMISION', 'Coordina admisión de pacientes'),
                                                (6, 'ADMISION', 'Personal de admisión de pacientes'),
                                                (7, 'ENFERMERA(O)', 'Personal de enfermería'),
                                                (8, 'PSICOLOGA(O)', 'Profesional de psicología'),
                                                (9, 'NUTRICIONISTA', 'Profesional de nutrición'),
                                                (10, 'TERAPISTA_DE_LENGUAJE', 'Profesional de terapia de lenguaje'),
                                                (11, 'TERAPISTA_FISICO_Y_DE_REHABILITACION', 'Profesional de rehabilitación física'),
                                                (12, 'GESTORA_DE_CITA', 'Gestor de citas médicas'),
                                                (13, 'COORDINADOR(A)_DE_GESTOR_DE_CITA', 'Coordina gestión de citas'),
                                                (14, 'COORDINADOR(A)_DE_TELEAPOYO_AL_DIAGNOSTICO', 'Coordina teleapoyo diagnóstico'),
                                                (15, 'COORDINADOR_DE_TELEURGENCIAS', 'Coordina teleurgencias'),
                                                (16, 'ADMINISTRATIVO', 'Personal administrativo general'),
                                                (17, 'GESTOR_TERRITORIAL', 'Gestor territorial de recursos'),
                                                (18, 'ESTADISTICO', 'Analista de estadísticas');