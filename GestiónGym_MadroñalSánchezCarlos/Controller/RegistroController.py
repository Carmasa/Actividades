# Controller/RegistroController.py

from Model.conection import obtener_conexion
from Model.Registro import Registro
from datetime import datetime

class RegistroController:
    @staticmethod
    def registrar_cliente_por_admin(cliente_id, admin_id):
        """Crea un registro de que un admin registró a un cliente."""
        fecha = datetime.now().strftime("%Y-%m-%d")
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    INSERT INTO Registro (cliente_id, admin_id, fecha_registro)
                    VALUES (?, ?, ?)
                """, (cliente_id, admin_id, fecha))
                conn.commit()
                return cursor.lastrowid
        except Exception as e:
            print(f"❌ Error al registrar cliente por admin: {e}")
            return None

    @staticmethod
    def obtener_registros_por_cliente(cliente_id):
        """Devuelve todos los registros de un cliente."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    SELECT id, cliente_id, admin_id, fecha_registro
                    FROM Registro
                    WHERE cliente_id = ?
                """, (cliente_id,))
                filas = cursor.fetchall()
                return [Registro(*fila) for fila in filas]
        except Exception as e:
            print(f"❌ Error al obtener registros: {e}")
            return []