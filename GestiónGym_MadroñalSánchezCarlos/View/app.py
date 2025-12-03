# View/app.py

import customtkinter as ctk
from View.login_frame import LoginFrame
from View.menu_frame import MenuPrincipalFrame
from View.clientes_frame import ClientesFrame
from View.aparatos_frame import AparatosFrame
from View.sesiones_frame import SesionesFrame
from View.morosos_frame import MorososFrame
from View.pagos_frame import PagosFrame

class GymApp(ctk.CTk):
    def __init__(self):
        super().__init__()
        self.title("GymForTheMoment - Gestión")
        self.geometry("1280x720")
        self.resizable(False, False)
        ctk.set_appearance_mode("System")
        ctk.set_default_color_theme("blue")

        # Centrar ventana
        self.update_idletasks()
        width = self.winfo_width()
        height = self.winfo_height()
        x = (self.winfo_screenwidth() // 2) - (width // 2)
        y = (self.winfo_screenheight() // 2) - (height // 2)
        self.geometry(f"{width}x{height}+{x}+{y}")

        # Contenedor
        contenedor = ctk.CTkFrame(self)
        contenedor.pack(fill="both", expand=True)

        contenedor.grid_rowconfigure(0, weight=1)
        contenedor.grid_columnconfigure(0, weight=1)

        self.frames = {}
        for F in (LoginFrame, MenuPrincipalFrame, ClientesFrame, AparatosFrame, SesionesFrame, MorososFrame, PagosFrame):
            frame = F(parent=contenedor, controller=self)
            self.frames[F] = frame
            frame.grid(row=0, column=0, sticky="nsew")

        self.mostrar_frame(LoginFrame)

    def mostrar_frame(self, clase_frame):
        frame = self.frames[clase_frame]
        frame.tkraise()