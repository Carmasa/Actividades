# View/morosos_frame.py

import customtkinter as ctk
from Controller.ClienteController import ClienteController
from Controller.PagoController import PagoController
from datetime import datetime
from PIL import Image
from CTkMessagebox import CTkMessagebox
from tkinter import filedialog
from reportlab.lib.pagesizes import A4
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer, Image as RLImage
from reportlab.lib.styles import getSampleStyleSheet
import os

class MorososFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller
        self.clientes_morosos = []
        self._crear_ui()
        self._cargar_morosos()
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

        ctk.CTkLabel(header, text="Lista de Morosos", font=("Arial", 32, "bold"), anchor="center").pack(side="left", expand=True)

        barra_acciones = ctk.CTkFrame(self, height=50, fg_color="gray15")
        barra_acciones.pack(fill="x", padx=20, pady=10)

        ctk.CTkButton(
            barra_acciones,
            text="Exportar lista de morosos",
            fg_color="#FF9800",
            hover_color="#F57C00",
            command=self._exportar_a_pdf
        ).pack(side="right", padx=(10, 0))

        self.scroll_frame = ctk.CTkScrollableFrame(self)
        self.scroll_frame.pack(fill="both", expand=True, padx=20, pady=(0, 20))

        self._crear_encabezado()

    def _crear_encabezado(self):
        encabezado = ctk.CTkFrame(self.scroll_frame, fg_color="gray20")
        encabezado.pack(fill="x", pady=(0, 5))
        cols = ["Nombre", "Email", "Teléfono", "Último Pago", "Estado"]
        ancho = [200, 200, 150, 150, 150]
        for i, (col, w) in enumerate(zip(cols, ancho)):
            ctk.CTkLabel(encabezado, text=col, font=("Arial", 12, "bold"), width=w).grid(row=0, column=i, padx=2, pady=5)


    def tkraise(self, aboveThis=None):
        self._cargar_morosos()
        super().tkraise(aboveThis)

    def _cargar_morosos(self):
        pagos_pendientes = PagoController.obtener_pagos_pendientes_mes_actual()
        morosos_ids = {p.cliente_id for p in pagos_pendientes}
        todos_clientes = ClienteController.obtener_todos()
        self.clientes_morosos = [c for c in todos_clientes if c.id in morosos_ids]

        for widget in self.scroll_frame.winfo_children():
            widget.destroy()
        self._crear_encabezado()

        for cliente in self.clientes_morosos:
            fila = ctk.CTkFrame(self.scroll_frame)
            fila.pack(fill="x", pady=2)

            datos = [cliente.nombre, cliente.email or "-", cliente.telefono or "-", cliente.fecha_registro or "-", "Pendiente"]
            ancho = [200, 200, 150, 150, 150]
            for i, (dato, w) in enumerate(zip(datos, ancho)):
                color = "red" if i == 4 else "white"
                ctk.CTkLabel(fila, text=dato, width=w, text_color=color).grid(row=0, column=i, padx=2, pady=5)

    def _exportar_a_pdf(self):
        if not self.clientes_morosos:
            CTkMessagebox(title="Advertencia", message="No hay morosos para exportar.")
            return

        filepath = filedialog.asksaveasfilename(
            defaultextension=".pdf",
            filetypes=[("PDF files", "*.pdf")],
            title="Guardar lista de morosos"
        )
        if not filepath:
            return

        try:
            doc = SimpleDocTemplate(filepath, pagesize=A4)
            elements = []
            styles = getSampleStyleSheet()

            logo_path = "Resources/logo.png"
            if os.path.exists(logo_path):
                logo = RLImage(logo_path, width=80, height=80)
                elements.append(logo)
                elements.append(Spacer(1, 12))

            title = Paragraph("Lista de Morosos", styles['Title'])
            elements.append(title)
            elements.append(Spacer(1, 24))

            data = [["Nombre", "Email", "Teléfono", "Último Pago", "Estado"]]
            for c in self.clientes_morosos:
                data.append([
                    c.nombre,
                    c.email or "-",
                    c.telefono or "-",
                    c.fecha_registro or "-",
                    "Pendiente"
                ])

            table = Table(data, colWidths=[120, 120, 100, 100, 80])
            table.setStyle(TableStyle([
                ('BACKGROUND', (0, 0), (-1, 0), colors.red),
                ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
                ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
                ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
                ('FONTSIZE', (0, 0), (-1, -1), 9),
                ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
                ('BACKGROUND', (0, 1), (-1, -1), colors.beige),
                ('GRID', (0, 0), (-1, -1), 1, colors.black),
            ]))
            elements.append(table)
            doc.build(elements)
            CTkMessagebox(title="Éxito", message=f"Listado exportado a:\n{filepath}")
        except Exception as e:
            print(f"❌ Error al exportar PDF: {e}")
            CTkMessagebox(title="Error", message="No se pudo generar el PDF.")

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