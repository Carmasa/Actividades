# Model/Aparato.py

class Aparato:
    def __init__(self, tipo, nombre, cantidad):
        self.tipo = tipo
        self.nombre = nombre
        self.cantidad = cantidad

    def __repr__(self):
        return f"{self.nombre} ({self.tipo}) - {self.cantidad} uds"


EQUIPAMIENTO_GIMNASIO = [
    # 🏃 CARDIO
    Aparato("cardio", "Cinta de correr", 6),
    Aparato("cardio", "Bicicleta estática", 5),
    Aparato("cardio", "Bicicleta spinning", 5),
    Aparato("cardio", "Elíptica", 5),
    Aparato("cardio", "Remo", 4),
    Aparato("cardio", "Air Bike", 3),
    Aparato("cardio", "Cinta de escalar", 2),
    # 🏋️ FUERZA
    Aparato("fuerza", "Press de pecho", 3),
    Aparato("fuerza", "Chest fly", 2),
    Aparato("fuerza", "Lat Pulldown", 3),
    Aparato("fuerza", "Seated Row", 3),
    Aparato("fuerza", "Shoulder Press", 2),
    Aparato("fuerza", "Leg Press", 3),
    Aparato("fuerza", "Leg Extension", 2),
    Aparato("fuerza", "Abductor", 2),
    Aparato("fuerza", "Cable Crossover", 3),
    Aparato("fuerza", "Smith Machine", 2),
    Aparato("fuerza", "Máquina de glúteos", 2),
    Aparato("fuerza", "Máquina abdominal", 2),
    # 🏋️ PESAS LIBRES
    Aparato("pesas libres", "Mancuernas", 12),
    Aparato("pesas libres", "Barras olímpicas", 8),
    Aparato("pesas libres", "Kettlebells", 10),
    Aparato("pesas libres", "Banco de pesas ajustable", 6),
    Aparato("pesas libres", "Jaula de potencia", 3),
    Aparato("pesas libres", "Banco Scott", 2),
    Aparato("pesas libres", "Barra EZ", 4),
    Aparato("pesas libres", "TRX", 4),
    Aparato("pesas libres", "Balón medicinal", 8),
    Aparato("pesas libres", "Cajón pliométrico", 4),
    Aparato("pesas libres", "Rueda abdominal", 5),
    Aparato("pesas libres", "Bandas elásticas", 10),
]