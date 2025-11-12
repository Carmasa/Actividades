# Controller/AparatoController.py

from Model.conection import obtener_conexion

class AparatoController:
    @staticmethod
    def existen_aparatos():
        """Verifica si ya hay aparatos en la base de datos."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("SELECT COUNT(*) FROM Aparato")
                return cursor.fetchone()[0] > 0
        except Exception as e:
            print(f"❌ Error al verificar aparatos: {e}")
            return False

    @staticmethod
    def insertar_aparatos_iniciales(equipamiento):
        """
        Inserta aparatos iniciales a partir de una lista de objetos Aparato.
        Cada unidad se crea como registro individual (ej: 'Cinta 1', 'Cinta 2', ...).
        """
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                for equipo in equipamiento:
                    for i in range(1, equipo.cantidad + 1):
                        nombre_individual = f"{equipo.nombre} {i}"
                        cursor.execute("""
                            INSERT INTO Aparato (nombre, tipo, estado)
                            VALUES (?, ?, 'disponible')
                        """, (nombre_individual, equipo.tipo))
                conn.commit()
            print("✅ Aparatos iniciales insertados.")
        except Exception as e:
            print(f"❌ Error al insertar aparatos iniciales: {e}")

    @staticmethod
    def obtener_todos_aparatos():
        """Devuelve todos los aparatos de la base de datos."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("SELECT id, nombre, tipo, estado FROM Aparato ORDER BY tipo, nombre")
                return cursor.fetchall()
        except Exception as e:
            print(f"❌ Error al obtener aparatos: {e}")
            return []

    @staticmethod
    def obtener_aparatos_disponibles():
        """Devuelve solo los aparatos disponibles."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("SELECT id, nombre, tipo FROM Aparato WHERE estado = 'disponible'")
                return cursor.fetchall()
        except Exception as e:
            print(f"❌ Error al obtener aparatos disponibles: {e}")
            return []