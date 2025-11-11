# conexion.py

import sqlite3
from Aparato import EQUIPAMIENTO_GIMNASIO
from Admin import ADMINS_INICIALES
import hashlib
DB_NAME = "gymforthemomentDB.db"


# --- Configuración y Conexión ---
def obtener_conexion():
    """Obtiene una conexión a la base de datos."""
    return sqlite3.connect(DB_NAME)


def inicializar_db():
    """Crea todas las tablas si no existen, según el modelo E-R del gimnasio."""
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()

            # Tabla: Admin (encargado del mostrador)
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS Admin (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    email TEXT UNIQUE,
                    telefono TEXT,
                    contraseña TEXT 
                )
            """)

            # Tabla: Cliente
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

            # Tabla: Aparato (sin ubicación)
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS Aparato (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    tipo TEXT NOT NULL,
                    estado TEXT DEFAULT 'disponible' CHECK(estado IN ('disponible', 'ocupado'))
                )
            """)

            # Tabla: Sesion
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

            # Tabla: Pago
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

            # Tabla: Registro
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


# --- Funciones para verificar existencia ---
def admins_existen():
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT COUNT(*) FROM Admin")
            return cursor.fetchone()[0] > 0
    except sqlite3.Error:
        return False


def aparatos_existen():
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT COUNT(*) FROM Aparato")
            return cursor.fetchone()[0] > 0
    except sqlite3.Error:
        return False

def hash_contraseña(contraseña):
    """Devuelve el hash SHA-256 de una contraseña (como string hexadecimal)."""
    return hashlib.sha256(contraseña.encode()).hexdigest()

# --- Inserción de datos iniciales ---
def insertar_datos_iniciales():

    # 👤 Insertar admins iniciales
    if not admins_existen():
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                for admin in ADMINS_INICIALES:
                    contraseña_hasheada = hash_contraseña(admin.contraseña)
                    cursor.execute("""
                        INSERT INTO Admin (nombre, email, telefono, contraseña)
                        VALUES (?, ?, ?, ?)
                    """, (admin.nombre, admin.email, admin.telefono, contraseña_hasheada))
                conn.commit()
            print("✅ Admins iniciales insertados.")
        except sqlite3.Error as e:
            print(f"❌ Error al insertar admins: {e}")
    else:
        print("⏭️  Los admins ya existen en la base de datos.")

    # 🏋️ Insertar aparatos (cada unidad como registro individual)
    if not aparatos_existen():
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                for equipo in EQUIPAMIENTO_GIMNASIO:
                    for i in range(1, equipo.cantidad + 1):
                        nombre_individual = f"{equipo.nombre} {i}"
                        cursor.execute("""
                            INSERT INTO Aparato (nombre, tipo, estado)
                            VALUES (?, ?, 'disponible')
                        """, (nombre_individual, equipo.tipo))
                conn.commit()
            print("✅ Aparatos iniciales insertados.")
        except sqlite3.Error as e:
            print(f"❌ Error al insertar aparatos: {e}")
    else:
        print("⏭️  Los aparatos ya existen en la base de datos.")

def validar_credenciales(email, contraseña):
    contraseña_hasheada = hash_contraseña(contraseña)
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            cursor.execute("""
                SELECT nombre FROM Admin
                WHERE email = ? AND contraseña = ?
            """, (email, contraseña_hasheada))
            resultado = cursor.fetchone()
            return resultado[0] if resultado else None
    except sqlite3.Error as e:
        print(f"❌ Error al validar credenciales: {e}")
        return None

# --- Funciones de utilidad (sin cambios) ---
def obtener_clientes_morosos():
    try:
        from datetime import datetime
        mes_actual = datetime.now().strftime("%Y-%m")
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            cursor.execute("""
                SELECT c.id, c.nombre, c.email
                FROM Cliente c
                JOIN Pago p ON c.id = p.cliente_id
                WHERE p.mes = ? AND p.estado = 'pendiente'
            """, (mes_actual,))
            return cursor.fetchall()
    except sqlite3.Error as e:
        print(f"❌ Error al obtener morosos: {e}")
        return []


def obtener_sesiones_por_dia(dia_semana):
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            cursor.execute("""
                SELECT s.id, c.nombre AS cliente, a.nombre AS aparato, s.hora_inicio, s.estado
                FROM Sesion s
                JOIN Cliente c ON s.cliente_id = c.id
                JOIN Aparato a ON s.aparato_id = a.id
                WHERE s.fecha = ?
                ORDER BY s.hora_inicio
            """, (dia_semana,))
            return cursor.fetchall()
    except sqlite3.Error as e:
        print(f"❌ Error al obtener sesiones por día: {e}")
        return []