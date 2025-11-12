# Controller/AdminController.py

from Model.conection import obtener_conexion, hash_contraseña

class AdminController:
    @staticmethod
    def validar_credenciales(email, contraseña):
        """Valida credenciales de un admin."""
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
        except Exception as e:
            print(f"❌ Error al validar credenciales: {e}")
            return None

    @staticmethod
    def existen_admins():
        """Verifica si ya hay admins en la base de datos."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                cursor.execute("SELECT COUNT(*) FROM Admin")
                return cursor.fetchone()[0] > 0
        except Exception as e:
            print(f"❌ Error al verificar admins: {e}")
            return False

    @staticmethod
    def insertar_admins_iniciales(admins):
        """Inserta una lista de objetos Admin en la base de datos."""
        try:
            with obtener_conexion() as conn:
                cursor = conn.cursor()
                for admin in admins:
                    contraseña_hasheada = hash_contraseña(admin.contraseña)
                    cursor.execute("""
                        INSERT INTO Admin (nombre, email, telefono, contraseña)
                        VALUES (?, ?, ?, ?)
                    """, (admin.nombre, admin.email, admin.telefono, contraseña_hasheada))
                conn.commit()
            print("✅ Admins iniciales insertados.")
        except Exception as e:
            print(f"❌ Error al insertar admins iniciales: {e}")