# Model/Registro.py

class Registro:
    def __init__(self, id=None, cliente_id=None, admin_id=None, fecha_registro=None):
        self.id = id
        self.cliente_id = cliente_id
        self.admin_id = admin_id
        self.fecha_registro = fecha_registro  # Formato: 'YYYY-MM-DD'

    def __repr__(self):
        return f"Registro(id={self.id}, cliente_id={self.cliente_id}, admin_id={self.admin_id})"