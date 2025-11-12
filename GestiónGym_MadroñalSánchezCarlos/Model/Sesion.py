# Model/Sesion.py

class Sesion:
    def __init__(self, id=None, cliente_id=None, aparato_id=None, fecha=None, hora_inicio=None, duracion_minutos=30, estado="programada"):
        self.id = id
        self.cliente_id = cliente_id
        self.aparato_id = aparato_id
        self.fecha = fecha  # Formato: 'YYYY-MM-DD'
        self.hora_inicio = hora_inicio  # Formato: 'HH:MM'
        self.duracion_minutos = duracion_minutos
        self.estado = estado  # 'programada', 'completada', 'cancelada'

    def __repr__(self):
        return f"Sesion(id={self.id}, cliente_id={self.cliente_id}, aparato_id={self.aparato_id}, fecha='{self.fecha}', hora='{self.hora_inicio}')"