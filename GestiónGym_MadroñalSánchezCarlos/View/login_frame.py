# View/login_frame.py

import customtkinter as ctk
from PIL import Image
from CTkMessagebox import CTkMessagebox
from Controller.AdminController import AdminController
from View.menu_frame import MenuPrincipalFrame

class LoginFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller

        # Cargar logo
        try:
            logo_path = "Resources/logo.png"
            logo_image = Image.open(logo_path)
            logo_image = logo_image.resize((300, 300), Image.LANCZOS)
            self.logo = ctk.CTkImage(light_image=logo_image, dark_image=logo_image, size=(300, 300))
        except Exception as e:
            print(f"⚠️ No se pudo cargar el logo: {e}")
            self.logo = None

        # Contenedor central
        frame_central = ctk.CTkFrame(self, fg_color="transparent")
        frame_central.pack(expand=True, fill="both", padx=50, pady=20)

        ctk.CTkLabel(frame_central, text="🔒 Iniciar Sesión", font=("Arial", 20, "bold")).pack(pady=10)

        if self.logo:
            ctk.CTkLabel(frame_central, image=self.logo, text="").pack(pady=20)

        ctk.CTkLabel(frame_central, text="Email:", font=("Arial", 12)).pack()
        self.entry_email = ctk.CTkEntry(frame_central, width=250)
        self.entry_email.pack(pady=5)

        ctk.CTkLabel(frame_central, text="Contraseña:", font=("Arial", 12)).pack()
        self.entry_pass = ctk.CTkEntry(frame_central, width=250, show="*")
        self.entry_pass.pack(pady=5)

        ctk.CTkButton(
            frame_central,
            text="Entrar",
            command=self.validar_login,
            fg_color="#4CAF50",
            text_color="white",
            hover_color="#45a049",
            width=150
        ).pack(pady=20)

    def validar_login(self):
        email = self.entry_email.get()
        contraseña = self.entry_pass.get()

        if not email or not contraseña:
            CTkMessagebox(title="⚠️ Advertencia", message="Completa todos los campos.")
            return

        nombre_admin = AdminController.validar_credenciales(email, contraseña)
        if nombre_admin:
            CTkMessagebox(title="✅ Éxito", message=f"Bienvenido, {nombre_admin}!")
            self.controller.mostrar_frame(MenuPrincipalFrame)
        else:
            CTkMessagebox(title="❌ Error", message="Credenciales incorrectas.")