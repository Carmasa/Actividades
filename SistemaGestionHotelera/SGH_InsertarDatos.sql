USE GestionHotel;

INSERT INTO habitaciones (numero_habitacion, tipo, precio_base) VALUES
(101, 'Sencilla', 50.00),
(102, 'Sencilla', 50.00),
(103, 'Sencilla', 50.00),
(104, 'Sencilla', 50.00),
(105, 'Sencilla', 50.00),
(201, 'Doble', 75.00),
(202, 'Doble', 75.00),
(203, 'Doble', 75.00),
(204, 'Doble', 75.00),
(205, 'Doble', 75.00),
(206, 'Doble', 75.00),
(207, 'Doble', 75.00),
(301, 'Suite', 120.00),
(302, 'Suite', 120.00),
(303, 'Suite', 120.00),
(304, 'Suite', 120.00);

INSERT INTO limpieza (id_habitacion, estado_limpieza) VALUES
(1, 'Limpia'),
(2, 'Limpia'),
(3, 'Limpia'),
(4, 'Limpia'),
(5, 'Limpia'),
(6, 'Limpia'),
(7, 'Limpia'),
(8, 'Limpia'),
(9, 'Limpia'),
(10, 'Limpia'),
(11, 'Limpia'),
(12, 'Limpia'),
(13, 'Limpia'),
(14, 'Limpia'),
(15, 'Limpia'),
(16, 'Limpia');

-- Insertar estado de mantenimiento para cada habitación
INSERT INTO mantenimiento (id_habitacion, tarea_mantenimiento, fecha_inicio, fecha_fin, descripcion_tarea, estado) VALUES
(1, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(2, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(3, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(4, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(5, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(6, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(7, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(8, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(9, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(10, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(11, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(12, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(13, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(14, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(15, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido'),
(16, 'Ninguna', '2025-01-01', '2025-01-01', 'Sin mantenimiento programado', 'No requerido');

