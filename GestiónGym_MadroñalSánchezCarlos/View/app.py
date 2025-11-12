# View/app.py
import customtkinter as ctk
from View.login_frame import LoginFrame
from View.menu_frame import MenuPrincipalFrame
from View.clientes_frame import ClientesFrame

class GymApp(ctk.CTk):
    def __init__(self):
        super().__init__()
        self.title("GymForTheMoment - Gestión")
        self.geometry("1280x720")
        self.resizable(False, False)
        ctk.set_appearance_mode("System")
        ctk.set_default_color_theme("blue")

        # Crear contenedor para los frames
        contenedor = ctk.CTkFrame(self)
        contenedor.pack(fill="both", expand=True)

        # CONFIGURACIÓN para hacer que el grid del contenedor se expanda
        contenedor.grid_rowconfigure(0, weight=1)   # Fila 0 ocupa todo el alto
        contenedor.grid_columnconfigure(0, weight=1) # Columna 0 ocupa todo el ancho

        # Diccionario para almacenar los frames
        self.frames = {}

        for F in (LoginFrame, MenuPrincipalFrame, ClientesFrame):
            frame = F(parent=contenedor, controller=self)
            self.frames[F] = frame
            frame.grid(row=0, column=0, sticky="nsew")

        # Mostrar el frame de login al inicio
        self.mostrar_frame(LoginFrame)

    def mostrar_frame(self, clase_frame):
        """Muestra el frame indicado."""
        frame = self.frames[clase_frame]
        frame.tkraise()