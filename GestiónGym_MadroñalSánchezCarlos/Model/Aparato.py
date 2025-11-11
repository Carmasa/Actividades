class Aparato:
    def __init__(self, tipo, nombre, cantidad):
        self.tipo = tipo
        self.nombre = nombre
        self.cantidad = cantidad

    def __repr__(self):
        return f"{self.nombre} ({self.tipo}) - {self.cantidad} uds"


# Lista completa de equipamiento de gimnasio
EQUIPAMIENTO_GIMNASIO = [
    # 🏃‍♂️ CARDIO
    Aparato("cardio", "Cinta de correr (Treadmill)", 6),
    Aparato("cardio", "Bicicleta estática", 5),
    Aparato("cardio", "Bicicleta de spinning", 5),
    Aparato("cardio", "Elíptica", 5),
    Aparato("cardio", "Remo (Rowing Machine)", 4),
    Aparato("cardio", "Escaladora / Stepper", 3),
    Aparato("cardio", "Air Bike / Assault Bike", 3),
    Aparato("cardio", "Cinta de escalar (StairMill)", 2),
    Aparato("cardio", "Esquí ergómetro (SkiErg)", 2),

    # 🏋️‍♂️ FUERZA GUIADA
    Aparato("fuerza", "Pectoral machine / Press de pecho", 3),
    Aparato("fuerza", "Chest fly / Aperturas de pecho", 2),
    Aparato("fuerza", "Lat Pulldown (jalón al pecho)", 3),
    Aparato("fuerza", "Seated Row (remo sentado)", 3),
    Aparato("fuerza", "Shoulder Press (press de hombros)", 2),
    Aparato("fuerza", "Leg Press (prensa de piernas)", 3),
    Aparato("fuerza", "Leg Extension (extensión de piernas)", 2),
    Aparato("fuerza", "Leg Curl (curl femoral)", 2),
    Aparato("fuerza", "Abductor / Adductor", 2),
    Aparato("fuerza", "Cable Crossover / Poleas ajustables", 3),
    Aparato("fuerza", "Smith Machine (máquina Smith)", 2),
    Aparato("fuerza", "Máquina de glúteos (Glute Kickback)", 2),
    Aparato("fuerza", "Máquina abdominal / Crunch Machine", 2),

    # 🏋️‍♀️ PESAS LIBRES Y FUNCIONAL
    Aparato("pesas libres", "Mancuernas (Dumbbells)", 12),
    Aparato("pesas libres", "Barras olímpicas", 8),
    Aparato("pesas libres", "Discos / Platos", 20),
    Aparato("pesas libres", "Kettlebells (pesas rusas)", 10),
    Aparato("pesas libres", "Banco de pesas ajustable", 6),
    Aparato("pesas libres", "Jaula de potencia (Power Rack)", 3),
    Aparato("pesas libres", "Banco Scott", 2),
    Aparato("pesas libres", "Barra EZ", 4),
    Aparato("pesas libres", "TRX / Cintas de suspensión", 4),
    Aparato("pesas libres", "Balón medicinal / Slam Ball", 8),
    Aparato("pesas libres", "Cajón pliométrico", 4),
    Aparato("pesas libres", "Rueda abdominal", 5),
    Aparato("pesas libres", "Bandas elásticas / Minibands", 10),
]
