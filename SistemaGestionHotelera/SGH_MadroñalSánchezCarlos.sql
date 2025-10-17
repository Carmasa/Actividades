DROP DATABASE IF EXISTS GestionHotel;
CREATE DATABASE GestionHotel;
USE GestionHotel;

CREATE TABLE habitaciones (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    numero_habitacion INT UNIQUE NOT NULL,
    tipo VARCHAR(50),
    precio_base DECIMAL(8, 2) NOT NULL,
    disponibilidad VARCHAR(50) DEFAULT 'Disponible' NOT NULL
);

CREATE TABLE huespedes (
    id_huesped INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    correo VARCHAR(100) UNIQUE NOT NULL,
    dni VARCHAR(9) UNIQUE NOT NULL
);

CREATE TABLE reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_huesped INT,
    id_habitacion INT,
    fecha_llegada DATE NOT NULL,
    fecha_salida DATE NOT NULL,
    precio_total DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(50) DEFAULT 'Pendiente',
    fecha_reserva DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_huesped) REFERENCES huespedes(id_huesped),
    FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion)
);

CREATE TABLE mantenimiento (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_habitacion INT,
    tarea_mantenimiento VARCHAR (150),
	fecha_inicio DATE NOT NULL,
    fecha_fin  DATE NOT NULL,
    descripcion_tarea VARCHAR (150),
	estado VARCHAR(50) DEFAULT 'Pendiente',
	FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion)
);

CREATE TABLE limpieza (
    id_limpieza INT AUTO_INCREMENT PRIMARY KEY,
    id_habitacion INT,
    estado_limpieza VARCHAR (50) DEFAULT 'Pendiente',
	FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion)
);



