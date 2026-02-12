# Task Management System - Proyecto Conjunto 1º & 2º DAM

Este es un proyecto colaborativo para la gestión de tareas, desarrollado en Java utilizando JavaFX para la interfaz gráfica y MySQL como sistema de gestión de bases de datos.

## Estructura del Proyecto

El proyecto sigue una arquitectura **MVC (Modelo-Vista-Controlador)** y utiliza el patrón **DAO (Data Access Object)** para la persistencia de datos.

- `model/`: Clases de entidad (`Usuario`, `Proyecto`, `Tarea`).
- `view/`: Archivos FXML para la interfaz.
- `controller/`: Lógica de control para cada vista.
- `dao/`: Clases para la conexión JDBC y operaciones CRUD.

## Requisitos

- **Java 21**
- **Maven**
- **MySQL Server** (XAMPP recomendado)

## Configuración de la Base de Datos

1. Importar el archivo `database_setup.sql` en MySQL.
2. Ajustar las credenciales en `DBConnection.java` si es necesario (por defecto: usuario `root`, contraseña vacía).

## Cómo Ejecutar

```bash
mvn clean javafx:run
```

## Funcionalidades

- Login de usuarios.
- Creación y listado de proyectos.
- Gestión de tareas por proyecto (Añadir tareas con estado y fecha límite).
