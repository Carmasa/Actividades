-- Esquema de Base de Datos para Blog CMS

CREATE DATABASE IF NOT EXISTS blog_cms;
USE blog_cms;

-- Tabla Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('admin', 'user') DEFAULT 'user',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Posts
CREATE TABLE IF NOT EXISTS posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    contenido TEXT NOT NULL,
    imagen VARCHAR(255),
    marcado BOOLEAN DEFAULT FALSE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla Etiquetas
CREATE TABLE IF NOT EXISTS etiquetas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla Post_Etiquetas (Muchos a Muchos)
CREATE TABLE IF NOT EXISTS post_etiquetas (
    post_id INT NOT NULL,
    etiqueta_id INT NOT NULL,
    PRIMARY KEY (post_id, etiqueta_id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (etiqueta_id) REFERENCES etiquetas(id) ON DELETE CASCADE
);

-- Tabla Palabras Prohibidas
CREATE TABLE IF NOT EXISTS palabras_prohibidas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    palabra VARCHAR(50) NOT NULL UNIQUE
);

-- Datos de Prueba
-- La contraseña es 'password123'
INSERT INTO usuarios (nombre_usuario, email, contrasena, rol) VALUES 
('admin', 'admin@blog.com', '$2y$10$Bs8Z1BIta0dL/hovLz0dsObZe5iS8szBmXP2seRfpl8harakGbSay', 'admin'),
('usuario', 'user@blog.com', '$2y$10$Bs8Z1BIta0dL/hovLz0dsObZe5iS8szBmXP2seRfpl8harakGbSay', 'user');

INSERT INTO palabras_prohibidas (palabra) VALUES ('spam'), ('prohibido'), ('ofensivo');
