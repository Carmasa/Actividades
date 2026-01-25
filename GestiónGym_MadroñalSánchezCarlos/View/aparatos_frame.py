# View/aparatos_frame.py

import customtkinter as ctk
from Controller.AparatoController import AparatoController
from Controller.SesionController import SesionController
from PIL import Image
from View.reserva_aparato import ReservaAparato
import re

class AparatosFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller
        self.aparatos = []
        self.filtro_tipo = "Todos"
        self._crear_ui()
        self._cargar_aparatos()
        self._crear_boton_volver()

    def _crear_ui(self):
        header = ctk.CTkFrame(self, height=80, fg_color="transparent")
        header.pack(fill="x", padx=20, pady=(20, 0))

        try:
            logo_path = "Resources/logo.png"
            logo_image = Image.open(logo_path)
            logo_image = logo_image.resize((60, 60), Image.LANCZOS)
            self.logo = ctk.CTkImage(light_image=logo_image, dark_image=logo_image, size=(60, 60))
            ctk.CTkLabel(header, image=self.logo, text="").pack(side="left", padx=(0, 20))
        except Exception as e:
            print(f"⚠️ No se pudo cargar el logo: {e}")
            ctk.CTkLabel(header, text="GYM", font=("Arial", 16)).pack(side="left", padx=(0, 20))

        ctk.CTkLabel(header, text="APARATOS", font=("Arial", 32, "bold"), anchor="center").pack(side="left", expand=True)

        barra_filtros = ctk.CTkFrame(self, height=50, fg_color="gray15")
        barra_filtros.pack(fill="x", padx=20, pady=(10, 10))

        for tipo in ["Todos", "Cardio", "Fuerza", "Pesas libres"]:
            btn = ctk.CTkButton(
                barra_filtros,
                text=tipo,
                command=lambda t=tipo: self._filtrar_aparatos(t),
                fg_color="#4CAF50" if tipo == "Todos" else "#3498db",
                hover_color="#45a049" if tipo == "Todos" else "#2980b9"
            )
            btn.pack(side="left", padx=5)

        self.scroll_frame = ctk.CTkScrollableFrame(self)
        self.scroll_frame.pack(fill="both", expand=True, padx=20, pady=(0, 20))

    def tkraise(self, aboveThis=None):
        self._cargar_aparatos()
        super().tkraise(aboveThis)

    def _cargar_aparatos(self):
        todos = AparatoController.obtener_todos_aparatos()
        seen = set()
        self.aparatos = []
        for id_aparato, nombre, tipo, estado in todos:
            partes = nombre.split()
            if len(partes) > 1 and partes[-1].isdigit():
                nombre_base = ' '.join(partes[:-1])
            else:
                nombre_base = nombre
            if nombre_base not in seen:
                seen.add(nombre_base)
                self.aparatos.append((id_aparato, nombre_base, tipo, estado))
        self._mostrar_tarjetas()

    def _filtrar_aparatos(self, tipo):
        self.filtro_tipo = tipo
        self._mostrar_tarjetas()

    @staticmethod
    def nombre_a_archivo(nombre):
        nombre_sin_paren = re.sub(r'\([^)]*\)', '', nombre)
        return (
            nombre_sin_paren.strip()
            .replace(" ", "_")
            .replace("/", "_")
            .replace(":", "")
            .replace(",", "")
            .replace("'", "")
            .replace('"', "")
            .replace("(", "")
            .replace(")", "")
            .replace("-", "_")
            .replace("__", "_")
            + ".jpg"
        )

    def _mostrar_tarjetas(self):
        for widget in self.scroll_frame.winfo_children():
            widget.destroy()

        aparatos_filtrados = self.aparatos
        if self.filtro_tipo != "Todos":
            aparatos_filtrados = [a for a in aparatos_filtrados if a[2] == self.filtro_tipo.lower()]

        if not aparatos_filtrados:
            ctk.CTkLabel(self.scroll_frame, text="No hay aparatos disponibles.", font=("Arial", 14)).pack(pady=20)
            return

        container = ctk.CTkFrame(self.scroll_frame, fg_color="transparent")
        container.pack(expand=True, fill="both", padx=20, pady=20)

        for col in range(4):
            container.grid_columnconfigure(col, weight=1)

        for i, aparato in enumerate(aparatos_filtrados):
            id_aparato, nombre, tipo, estado = aparato
            row = i // 4
            col = i % 4

            card = ctk.CTkFrame(container, width=200, height=250, corner_radius=10, fg_color="gray20")
            card.grid(row=row, column=col, padx=10, pady=10, sticky="nsew")

            if tipo == "cardio":
                tipo_carpeta = "Cardio"
            elif tipo == "fuerza":
                tipo_carpeta = "Fuerza"
            elif tipo == "pesas libres":
                tipo_carpeta = "Peso_libre"
            else:
                tipo_carpeta = "Cardio"

            img_nombre = AparatosFrame.nombre_a_archivo(nombre)
            img_path = f"Resources/aparatos/{tipo_carpeta}/{img_nombre}"
            try:
                img = Image.open(img_path)
                img = img.resize((160, 160), Image.LANCZOS)
                photo = ctk.CTkImage(light_image=img, dark_image=img, size=(160, 160))
                ctk.CTkLabel(card, image=photo, text="").pack(pady=(10, 5))
                card.image = photo
            except Exception as e:
                print(f"⚠️ No se pudo cargar {img_path}: {e}")
                ctk.CTkLabel(card, text="📷", font=("Arial", 30)).pack(pady=(10, 5))

            ctk.CTkLabel(card, text=nombre, font=("Arial", 12, "bold"), wraplength=180).pack()
            color = "green" if estado == "disponible" else "red"
            ctk.CTkLabel(card, text=f"Estado: {estado}", text_color=color, font=("Arial", 10)).pack(pady=(5, 10))

            card.bind("<Button-1>", lambda e, a=aparato: self._abrir_reserva(a))
            for child in card.winfo_children():
                child.bind("<Button-1>", lambda e, a=aparato: self._abrir_reserva(a))

    def _abrir_reserva(self, aparato):
        ReservaAparato(self, aparato, self._sesion_creada)

    def _sesion_creada(self):
        pass

    def _crear_boton_volver(self):
        try:
            img_path = "Resources/volver.png"
            img = Image.open(img_path)
            img = img.resize((48, 48), Image.LANCZOS)
            icono_volver = ctk.CTkImage(light_image=img, dark_image=img, size=(48, 48))
            btn_volver = ctk.CTkButton(
                self,
                image=icono_volver,
                text="",
                width=30,
                height=30,
                fg_color="transparent",
                hover_color="gray30",
                command=self._volver_al_menu
            )
            btn_volver.place(relx=1.0, rely=0.0, x=-20, y=20, anchor="ne")
        except Exception as e:
            print(f"⚠️ No se pudo cargar el ícono volver: {e}")
            btn_volver = ctk.CTkButton(
                self,
                text="⬅️ Volver",
                width=80,
                height=30,
                fg_color="transparent",
                hover_color="gray30",
                command=self._volver_al_menu
            )
            btn_volver.place(relx=1.0, rely=0.0, x=-20, y=20, anchor="ne")

    def _volver_al_menu(self):
        from View.menu_frame import MenuPrincipalFrame
        self.controller.mostrar_frame(MenuPrincipalFrame)