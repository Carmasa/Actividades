# Controller/PagoController.py

from Model.conection import obtener_conexion
from Model.Pago import Pago
from datetime import datetime

class PagoController:
    @staticmethod
    def registrar_pago_mensual(cliente_id, mes, monto):
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    INSERT INTO Pago (cliente_id, mes, monto)
                    VALUES (?, ?, ?)
                """, (cliente_id, mes, monto))
                conn.commit()
                return cursor.lastrowid
        except Exception as e:
            print(f"❌ Error al registrar pago: {e}")
            return None

    @staticmethod
    def marcar_como_pagado(pago_id, fecha_pago=None):
        if fecha_pago is None:
            fecha_pago = datetime.now().strftime("%Y-%m-%d")
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    UPDATE Pago
                    SET estado = 'pagado', fecha_pago = ?
                    WHERE id = ?
                """, (fecha_pago, pago_id))
                conn.commit()
                return cursor.rowcount > 0
        except Exception as e:
            print(f"❌ Error al marcar pago: {e}")
            return False

    @staticmethod
    def obtener_pagos_pendientes_mes_actual():
        mes_actual = datetime.now().strftime("%Y-%m")
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    SELECT id, cliente_id, mes, monto, fecha_pago, estado
                    FROM Pago
                    WHERE mes = ? AND estado = 'pendiente'
                """, (mes_actual,))
                filas = cursor.fetchall()
                return [Pago(*fila) for fila in filas]
        except Exception as e:
            print(f"❌ Error al obtener pagos pendientes: {e}")
            return []

    @staticmethod
    def obtener_morosos_por_mes(mes):
        """Devuelve lista de clientes con pago pendiente en un mes específico."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    SELECT c.id, c.nombre, c.email, c.telefono, c.fecha_registro,
                           p.id as pago_id, p.monto
                    FROM Cliente c
                    JOIN Pago p ON c.id = p.cliente_id
                    WHERE p.mes = ? AND p.estado = 'pendiente'
                    ORDER BY c.nombre
                """, (mes,))
                return cursor.fetchall()  # Lista de tuplas
        except Exception as e:
            print(f"❌ Error al obtener morosos por mes: {e}")
            return []


    @staticmethod
    def obtener_todos_los_pagos_del_mes(mes):
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("""
                    SELECT id, cliente_id, mes, monto, fecha_pago, estado
                    FROM Pago
                    WHERE mes = ?
                """, (mes,))
                filas = cursor.fetchall()
                return [Pago(*fila) for fila in filas]
        except Exception as e:
            print(f"❌ Error al obtener todos los pagos del mes: {e}")
            return []