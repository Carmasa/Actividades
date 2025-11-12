# Controller/ClienteController.py

from Model.conection import obtener_conexion
from Model.Cliente import Cliente
from datetime import datetime

class ClienteController:
    @staticmethod
    def crear_cliente(nombre, email, telefono):
        """Registra un nuevo cliente en la base de datos."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    INSERT INTO Cliente (nombre, email, telefono)
                    VALUES (?, ?, ?)
                """, (nombre, email, telefono))
                conn.commit()
                return cursor.lastrowid  # Devuelve el ID del nuevo cliente
        except Exception as e:
            print(f"❌ Error al crear cliente: {e}")
            return None

    @staticmethod
    def obtener_todos():
        """Devuelve una lista de objetos Cliente."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("SELECT id, nombre, email, telefono, fecha_registro, activo, moroso FROM Cliente")
                filas = cursor.fetchall()
                return [Cliente(*fila) for fila in filas]
        except Exception as e:
            print(f"❌ Error al obtener clientes: {e}")
            return []

    @staticmethod
    def obtener_morosos():
        """Devuelve clientes morosos del mes actual."""
        mes_actual = datetime.now().strftime("%Y-%m")
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    SELECT c.id, c.nombre, c.email, c.telefono, c.fecha_registro, c.activo, c.moroso
                    FROM Cliente c
                    JOIN Pago p ON c.id = p.cliente_id
                    WHERE p.mes = ? AND p.estado = 'pendiente'
                """, (mes_actual,))
                filas = cursor.fetchall()
                return [Cliente(*fila) for fila in filas]
        except Exception as e:
            print(f"❌ Error al obtener morosos: {e}")
            return []