-- Create Database
CREATE DATABASE IF NOT EXISTS task_manager_db;
USE task_manager_db;

-- Create Usuarios table
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create Proyectos table
CREATE TABLE IF NOT EXISTS proyectos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Create Tareas table
CREATE TABLE IF NOT EXISTS tareas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    proyecto_id INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    estado ENUM('Pendiente', 'En Progreso', 'Completada') DEFAULT 'Pendiente',
    fecha_limite DATE,
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id) ON DELETE CASCADE
);

-- Insert dummy data for testing
INSERT INTO usuarios (username, password) VALUES ('admin', 'admin123');
INSERT INTO usuarios (username, password) VALUES ('user1', 'pass123');

INSERT INTO proyectos (usuario_id, nombre, descripcion) VALUES (1, 'Proyecto DAM', 'Actividad conjunta 1º y 2º DAM');
INSERT INTO tareas (proyecto_id, titulo, estado, fecha_limite) VALUES (1, 'Diseño DB', 'Completada', '2024-02-10');
INSERT INTO tareas (proyecto_id, titulo, estado, fecha_limite) VALUES (1, 'Desarrollo UI', 'Pendiente', '2024-02-15');
