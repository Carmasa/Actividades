# Model/Cliente.py

class Cliente:
    def __init__(self, id=None, nombre=None, email=None, telefono=None, fecha_registro=None, activo=True, moroso=False):
        self.id = id
        self.nombre = nombre
        self.email = email
        self.telefono = telefono
        self.fecha_registro = fecha_registro
        self.activo = activo
        self.moroso = moroso

    def __repr__(self):
        return f"Cliente(id={self.id}, nombre='{self.nombre}', email='{self.email}')"