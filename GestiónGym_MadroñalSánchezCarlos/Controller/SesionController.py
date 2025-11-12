# Controller/SesionController.py

from Model.conection import obtener_conexion
from Model.Sesion import Sesion
from datetime import datetime

class SesionController:
    @staticmethod
    def crear_sesion(cliente_id, aparato_id, fecha, hora_inicio):
        """Registra una nueva sesión de 30 minutos."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    INSERT INTO Sesion (cliente_id, aparato_id, fecha, hora_inicio)
                    VALUES (?, ?, ?, ?)
                """, (cliente_id, aparato_id, fecha, hora_inicio))
                conn.commit()
                return cursor.lastrowid
        except Exception as e:
            print(f"❌ Error al crear sesión: {e}")
            return None

    @staticmethod
    def obtener_sesiones_por_dia(fecha):
        """Devuelve todas las sesiones programadas para una fecha específica."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    SELECT id, cliente_id, aparato_id, fecha, hora_inicio, duracion_minutos, estado
                    FROM Sesion
                    WHERE fecha = ?
                    ORDER BY hora_inicio
                """, (fecha,))
                filas = cursor.fetchall()
                return [Sesion(*fila) for fila in filas]
        except Exception as e:
            print(f"❌ Error al obtener sesiones: {e}")
            return []

    @staticmethod
    def existe_sesion_en_horario(aparato_id, fecha, hora_inicio):
        """Verifica si un aparato ya está reservado en esa fecha y hora."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    SELECT 1 FROM Sesion
                    WHERE aparato_id = ? AND fecha = ? AND hora_inicio = ? AND estado != 'cancelada'
                """, (aparato_id, fecha, hora_inicio))
                return cursor.fetchone() is not None
        except Exception as e:
            print(f"❌ Error al verificar disponibilidad: {e}")
            return False