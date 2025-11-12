# Model/conection.py

import sqlite3
import hashlib

DB_NAME = "gymforthemomentDB.db"

def obtener_conexion():
    return sqlite3.connect(DB_NAME)

def hash_contraseña(contraseña):
    return hashlib.sha256(contraseña.encode()).hexdigest()

def inicializar_db():
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()

            cursor.execute("""
                CREATE TABLE IF NOT EXISTS Admin (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    email TEXT UNIQUE,
                    telefono TEXT,
                    contraseña TEXT
                )
            """)

            cursor.execute("""
                CREATE TABLE IF NOT EXISTS Cliente (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    email TEXT UNIQUE,
                    telefono TEXT,
                    fecha_registro DATE DEFAULT (date('now')),
                    activo BOOLEAN DEFAULT 1,
                    moroso BOOLEAN DEFAULT 0
                )
            """)

            cursor.execute("""
                CREATE TABLE IF NOT EXISTS Aparato (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    tipo TEXT NOT NULL,
                    estado TEXT DEFAULT 'disponible' CHECK(estado IN ('disponible', 'ocupado'))
                )
            """)

            cursor.execute("""
                CREATE TABLE IF NOT EXISTS Sesion (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente_id INTEGER NOT NULL,
                    aparato_id INTEGER NOT NULL,
                    fecha DATE NOT NULL,
                    hora_inicio TIME NOT NULL,
                    duracion_minutos INTEGER DEFAULT 30,
                    estado TEXT DEFAULT 'programada' CHECK(estado IN ('programada', 'completada', 'cancelada')),
                    FOREIGN KEY (cliente_id) REFERENCES Cliente(id) ON DELETE CASCADE,
                    FOREIGN KEY (aparato_id) REFERENCES Aparato(id) ON DELETE CASCADE
                )
            """)

            cursor.execute("""
                CREATE TABLE IF NOT EXISTS Pago (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente_id INTEGER NOT NULL,
                    mes TEXT NOT NULL,
                    monto REAL NOT NULL,
                    fecha_pago DATE,
                    estado TEXT DEFAULT 'pendiente' CHECK(estado IN ('pendiente', 'pagado')),
                    FOREIGN KEY (cliente_id) REFERENCES Cliente(id) ON DELETE CASCADE
                )
            """)

            cursor.execute("""
                CREATE TABLE IF NOT EXISTS Registro (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente_id INTEGER NOT NULL,
                    admin_id INTEGER NOT NULL,
                    fecha_registro DATE DEFAULT (date('now')),
                    FOREIGN KEY (cliente_id) REFERENCES Cliente(id) ON DELETE CASCADE,
                    FOREIGN KEY (admin_id) REFERENCES Admin(id) ON DELETE CASCADE
                )
            """)

            conn.commit()
        print("✅ Base de datos y tablas inicializadas correctamente.")
    except sqlite3.Error as e:
        print(f"❌ Error al inicializar la DB: {e}")