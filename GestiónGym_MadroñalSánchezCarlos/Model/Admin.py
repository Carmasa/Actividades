class Admin:
    def __init__(self, nombre, email, telefono, contraseña):
        self.nombre = nombre
        self.email = email
        self.telefono = telefono
        self.contraseña = contraseña


ADMINS_INICIALES = [
    Admin("Ana López", "ana@gym.com", "600111222", "ana123"),
    Admin("Carlos Ruiz", "carlos@gym.com", "600333444", "carlos123"),
    Admin("Lucía Mendoza", "lucia@gym.com", "600555666", "lucia123"),
]