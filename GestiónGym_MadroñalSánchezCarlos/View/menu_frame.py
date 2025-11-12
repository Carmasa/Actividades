# View/menu_frame.py
import customtkinter as ctk
from View.clientes_frame import ClientesFrame

class MenuPrincipalFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller

        frame_central = ctk.CTkFrame(self, fg_color="transparent")
        frame_central.pack(expand=True, fill="both", padx=50, pady=50)

        ctk.CTkLabel(frame_central, text="📋 Menú Principal", font=("Arial", 16, "bold")).pack(pady=20)

        botones = [
            ("Gestión de Clientes", self.ir_a_clientes),
            ("Reservar Sesión", self.ir_a_sesiones),
            ("Control de Aparatos", self.ir_a_aparatos),
            ("Pagos y Morosos", self.ir_a_pagos),
            ("Cerrar Sesión", self.cerrar_sesion),
        ]

        for texto, comando in botones:
            ctk.CTkButton(
                frame_central,
                text=texto,
                width=250,
                command=comando
            ).pack(pady=10)

    def ir_a_clientes(self):
        from View.clientes_frame import ClientesFrame
        self.controller.mostrar_frame(ClientesFrame)

    def ir_a_sesiones(self):
        print("Ir a reservar sesión")

    def ir_a_aparatos(self):
        print("Ir a control de aparatos")

    def ir_a_pagos(self):
        print("Ir a pagos y morosos")

    def cerrar_sesion(self):
        from View.login_frame import LoginFrame
        self.controller.mostrar_frame(LoginFrame)