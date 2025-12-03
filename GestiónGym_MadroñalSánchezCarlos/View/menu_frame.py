# View/menu_frame.py

import customtkinter as ctk
from PIL import Image
from View.clientes_frame import ClientesFrame
from View.aparatos_frame import AparatosFrame
from View.sesiones_frame import SesionesFrame
from View.morosos_frame import MorososFrame
from View.pagos_frame import PagosFrame

class MenuPrincipalFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller
        self._crear_ui()

    def _crear_ui(self):
        header = ctk.CTkFrame(self, height=100, fg_color="transparent")
        header.pack(fill="x", padx=20, pady=(20, 10))

        try:
            logo_path = "../Resources/logo.png"
            logo_image = Image.open(logo_path)
            logo_image = logo_image.resize((120, 120), Image.LANCZOS)
            self.logo = ctk.CTkImage(light_image=logo_image, dark_image=logo_image, size=(120, 120))
            ctk.CTkLabel(header, image=self.logo, text="").pack()
        except Exception as e:
            print(f"⚠️ No se pudo cargar el logo: {e}")
            ctk.CTkLabel(header, text="GYM", font=("Arial", 20)).pack()

        container = ctk.CTkFrame(self, fg_color="transparent")
        container.pack(expand=True, fill="both", padx=20, pady=20)

        for col in range(5):
            container.grid_columnconfigure(col, weight=1)

        opciones = [
            ("Clientes", "Clientes.png", self.ir_a_clientes),
            ("Sesiones", "Sesiones.png", self.ir_a_sesiones),
            ("Aparatos", "Aparatos.png", self.ir_a_aparatos),
            ("Pagos", "Pagos.png", self.ir_a_pagos),
            ("Morosos", "Morosos.png", self.ir_a_morosos),
        ]

        for i, (titulo, img_nombre, comando) in enumerate(opciones):
            shadow = ctk.CTkFrame(container, fg_color="gray30", corner_radius=10)
            shadow.grid(row=0, column=i, padx=10, pady=10, sticky="nsew")

            card = ctk.CTkFrame(shadow, width=196, height=246, corner_radius=8, fg_color="gray20", border_width=2, border_color="gray50")
            card.pack(expand=True, fill="both", padx=2, pady=2)

            img_container = ctk.CTkFrame(card, fg_color="transparent")
            img_container.pack(expand=True, fill="both", padx=10, pady=(10, 5))

            img_path = f"../Resources/{img_nombre}"
            try:
                img = Image.open(img_path)
                img = img.resize((160, 160), Image.LANCZOS)
                photo = ctk.CTkImage(light_image=img, dark_image=img, size=(160, 160))
                img_label = ctk.CTkLabel(img_container, image=photo, text="")
                img_label.pack(expand=True, fill="both")
                card.image = photo
            except Exception as e:
                print(f"⚠️ No se pudo cargar {img_path}: {e}")
                ctk.CTkLabel(img_container, text="📷", font=("Arial", 30)).pack(expand=True, fill="both")

            title_label = ctk.CTkLabel(card, text=titulo, font=("Arial", 14, "bold"))
            title_label.pack(pady=(5, 10))

            def create_hover_effects(card_widget, title_label_widget):
                def on_enter(event):
                    card_widget.configure(fg_color="#3498db")
                    title_label_widget.configure(text_color="white")
                def on_leave(event):
                    card_widget.configure(fg_color="gray20")
                    title_label_widget.configure(text_color="black")
                return on_enter, on_leave

            on_enter_func, on_leave_func = create_hover_effects(card, title_label)

            card.bind("<Enter>", on_enter_func)
            card.bind("<Leave>", on_leave_func)
            img_label.bind("<Enter>", on_enter_func)
            img_label.bind("<Leave>", on_leave_func)
            title_label.bind("<Enter>", on_enter_func)
            title_label.bind("<Leave>", on_leave_func)

            card.bind("<Button-1>", lambda e, cmd=comando: cmd())
            img_label.bind("<Button-1>", lambda e, cmd=comando: cmd())
            title_label.bind("<Button-1>", lambda e, cmd=comando: cmd())

    def ir_a_clientes(self):
        self.controller.mostrar_frame(ClientesFrame)

    def ir_a_sesiones(self):
        self.controller.mostrar_frame(SesionesFrame)

    def ir_a_aparatos(self):
        self.controller.mostrar_frame(AparatosFrame)

    def ir_a_pagos(self):
        self.controller.mostrar_frame(PagosFrame)

    def ir_a_morosos(self):
        self.controller.mostrar_frame(MorososFrame)