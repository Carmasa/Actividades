# conexion.py

import sqlite3
import os

DB_NAME = "juego_peliculas.db"


# --- Configuración y Conexión ---
def obtener_conexion():
    return sqlite3.connect(DB_NAME)


def inicializar_db():
    """Crea todas las tablas si no existen."""
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()

            # Tabla de puntuaciones
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS puntuaciones (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    puntuacion INTEGER NOT NULL
                )
            """)

            # Tabla de películas
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS peliculas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL UNIQUE
                )
            """)

            # Tabla de pistas (relacionada con películas)
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS pistas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pelicula_id INTEGER NOT NULL,
                    texto TEXT NOT NULL,
                    FOREIGN KEY (pelicula_id) REFERENCES peliculas(id)
                )
            """)

            conn.commit()
        print("✅ Base de datos y tablas inicializadas.")
    except sqlite3.Error as e:
        print(f"❌ Error al inicializar la DB: {e}")


# --- Gestión de películas y pistas ---
def peliculas_existen():
    """Devuelve True si ya hay películas en la base de datos."""
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT COUNT(*) FROM peliculas")
            return cursor.fetchone()[0] > 0
    except sqlite3.Error as e:
        print(f"Error al verificar películas: {e}")
        return False


def insertar_peliculas_iniciales(lista_peliculas):
    """Inserta las películas y sus pistas si la tabla está vacía."""
    if peliculas_existen():
        print("🎬 Las películas ya están en la base de datos.")
        return

    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            for peli in lista_peliculas:
                # Insertar película
                cursor.execute("INSERT INTO peliculas (nombre) VALUES (?)", (peli.nombre,))
                pelicula_id = cursor.lastrowid
                # Insertar pistas
                for pista in peli.pistas:
                    cursor.execute("INSERT INTO pistas (pelicula_id, texto) VALUES (?, ?)",
                                   (pelicula_id, pista))
            conn.commit()
        print("✅ Películas y pistas insertadas en la base de datos.")
    except sqlite3.Error as e:
        print(f"❌ Error al insertar películas: {e}")


def obtener_pelicula_aleatoria():
    """Devuelve una película aleatoria con sus pistas desde la DB."""
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            # Obtener una película aleatoria
            cursor.execute("SELECT id, nombre FROM peliculas ORDER BY RANDOM() LIMIT 1")
            peli_row = cursor.fetchone()
            if not peli_row:
                return None
            peli_id, nombre = peli_row

            # Obtener sus pistas
            cursor.execute("SELECT texto FROM pistas WHERE pelicula_id = ? ORDER BY id", (peli_id,))
            pistas = [row[0] for row in cursor.fetchall()]

            return {"id": peli_id, "nombre": nombre, "pistas": pistas}
    except sqlite3.Error as e:
        print(f"Error al obtener película aleatoria: {e}")
        return None


# --- Gestión de puntuaciones (ya tenías esto) ---
def insertar_puntuacion(nombre, puntuacion):
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            cursor.execute(
                "INSERT INTO puntuaciones (nombre, puntuacion) VALUES (?, ?)",
                (nombre, puntuacion)
            )
            conn.commit()
            print(f"✅ Puntuación de '{nombre}' guardada.")
    except sqlite3.Error as e:
        print(f"❌ Error al insertar puntuación: {e}")


def leer_puntuaciones():
    try:
        with obtener_conexion() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT id, nombre, puntuacion FROM puntuaciones ORDER BY puntuacion DESC")
            return cursor.fetchall()
    except sqlite3.Error as e:
        print(f"Error al leer puntuaciones: {e}")
        return []