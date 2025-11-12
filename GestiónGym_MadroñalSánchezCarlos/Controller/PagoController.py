# Controller/PagoController.py

from Model.conection import obtener_conexion
from Model.Pago import Pago
from datetime import datetime

class PagoController:
    @staticmethod
    def registrar_pago_mensual(cliente_id, mes, monto):
        """Registra un pago pendiente para un cliente y mes."""
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
        """Marca un pago como completado."""
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
        """Devuelve los pagos pendientes del mes actual."""
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