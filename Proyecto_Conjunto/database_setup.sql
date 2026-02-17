-- Crear Base de Datos
CREATE DATABASE IF NOT EXISTS gestor_tareas_db;
USE gestor_tareas_db;

-- Tabla de Usuarios (Mantenida por compatibilidad mínima)
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

-- Tabla de Proyectos
CREATE TABLE IF NOT EXISTS proyectos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla de Tareas
CREATE TABLE IF NOT EXISTS tareas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    proyecto_id INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    completada BOOLEAN DEFAULT FALSE,
    fecha_limite DATE,
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id) ON DELETE CASCADE
);

-- Insertar datos por defecto
INSERT IGNORE INTO usuarios (id, nombre_usuario, contrasena) VALUES (1, 'admin', 'admin123');
INSERT IGNORE INTO proyectos (id, usuario_id, nombre, descripcion) VALUES (1, 1, 'Proyecto DAM', 'Proyecto único de gestión');

-- Tareas de ejemplo
INSERT INTO tareas (proyecto_id, titulo, completada, fecha_limite) VALUES (1, 'Diseñar Base de Datos', TRUE, '2024-02-20');
INSERT INTO tareas (proyecto_id, titulo, completada, fecha_limite) VALUES (1, 'Implementar Interfaz', FALSE, '2024-02-25');
