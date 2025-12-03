# View/sesiones_frame.py

import customtkinter as ctk
from Controller.SesionController import SesionController
from Controller.ClienteController import ClienteController
from Controller.AparatoController import AparatoController
from datetime import datetime, timedelta
from tkcalendar import Calendar
from reportlab.lib.pagesizes import A4
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer, Image as RLImage
from reportlab.lib.styles import getSampleStyleSheet
from CTkMessagebox import CTkMessagebox
from tkinter import filedialog
import os

class SesionesFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller
        self.fecha_seleccionada = datetime.now().strftime("%Y-%m-%d")
        self._crear_ui()
        self._cargar_sesiones()
        self._crear_boton_volver()

    def _crear_ui(self):
        header = ctk.CTkFrame(self, height=80, fg_color="transparent")
        header.pack(fill="x", padx=20, pady=(20, 0))

        try:
            from PIL import Image
            logo_path = "../Resources/logo.png"
            logo_image = Image.open(logo_path)
            logo_image = logo_image.resize((60, 60), Image.LANCZOS)
            self.logo = ctk.CTkImage(light_image=logo_image, dark_image=logo_image, size=(60, 60))
            ctk.CTkLabel(header, image=self.logo, text="").pack(side="left", padx=(0, 20))
        except Exception as e:
            print(f"⚠️ No se pudo cargar el logo: {e}")

        ctk.CTkLabel(header, text="Registro de sesiones", font=("Arial", 32, "bold")).pack(side="left", expand=True)

        barra = ctk.CTkFrame(self, height=50, fg_color="gray15")
        barra.pack(fill="x", padx=20, pady=10)

        ctk.CTkLabel(barra, text="Fecha:").pack(side="left", padx=(0, 5))
        self.entry_fecha = ctk.CTkEntry(barra, width=120)
        self.entry_fecha.insert(0, self.fecha_seleccionada)
        self.entry_fecha.pack(side="left", padx=(0, 10))

        ctk.CTkButton(barra, text="📅", width=30, command=self._abrir_calendario).pack(side="left", padx=(0, 10))

        dias = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes"]
        hoy = datetime.now().weekday()
        for i, dia in enumerate(dias):
            color = "#4CAF50" if i == hoy else "#3498db"
            ctk.CTkButton(
                barra,
                text=dia,
                command=lambda d=i: self._ir_a_dia(d),
                fg_color=color,
                hover_color="#45a049" if i == hoy else "#2980b9"
            ).pack(side="left", padx=5)

        ctk.CTkButton(
            barra,
            text="Exportar listado",
            fg_color="#FF9800",
            hover_color="#F57C00",
            command=self._exportar_a_pdf
        ).pack(side="right", padx=(10, 0))

        self.scroll_frame = ctk.CTkScrollableFrame(self)
        self.scroll_frame.pack(fill="both", expand=True, padx=20, pady=(0, 20))

        self._crear_encabezado()

    def _crear_encabezado(self):
        enc = ctk.CTkFrame(self.scroll_frame, fg_color="gray20")
        enc.pack(fill="x", pady=(0, 5))
        cols = ["Hora", "Nombre", "Aparato"]
        widths = [100, 200, 300]
        for i, (col, w) in enumerate(zip(cols, widths)):
            ctk.CTkLabel(enc, text=col, font=("Arial", 12, "bold"), width=w).grid(row=0, column=i, padx=2)

    def _cargar_sesiones(self):
        sesiones = SesionController.obtener_sesiones_por_dia(self.fecha_seleccionada)
        for widget in self.scroll_frame.winfo_children():
            widget.destroy()
        self._crear_encabezado()

        clientes = {c.id: c.nombre for c in ClienteController.obtener_todos()}
        aparatos = {a[0]: a[1] for a in AparatoController.obtener_todos_aparatos()}

        for s in sesiones:
            fila = ctk.CTkFrame(self.scroll_frame)
            fila.pack(fill="x", pady=2)
            ctk.CTkLabel(fila, text=s.hora_inicio, width=100).grid(row=0, column=0, padx=2)
            ctk.CTkLabel(fila, text=clientes.get(s.cliente_id, "Desconocido"), width=200).grid(row=0, column=1, padx=2)
            ctk.CTkLabel(fila, text=aparatos.get(s.aparato_id, "Desconocido"), width=300).grid(row=0, column=2, padx=2)

    def tkraise(self, aboveThis=None):
        self.fecha_seleccionada = datetime.now().strftime("%Y-%m-%d")
        self.entry_fecha.delete(0, "end")
        self.entry_fecha.insert(0, self.fecha_seleccionada)
        self._cargar_sesiones()
        super().tkraise(aboveThis)

    def _ir_a_dia(self, dia_index):
        hoy = datetime.now()
        inicio_semana = hoy - timedelta(days=hoy.weekday())
        fecha_dia = inicio_semana + timedelta(days=dia_index)
        self.fecha_seleccionada = fecha_dia.strftime("%Y-%m-%d")
        self.entry_fecha.delete(0, "end")
        self.entry_fecha.insert(0, self.fecha_seleccionada)
        self._cargar_sesiones()

    def _abrir_calendario(self):
        top = ctk.CTkToplevel(self)
        top.title("Seleccionar fecha")
        top.geometry("300x300")
        top.grab_set()

        cal = Calendar(top, selectmode="day", date_pattern="y-mm-dd")
        cal.pack(pady=10)

        def confirmar():
            self.fecha_seleccionada = cal.get_date()
            self.entry_fecha.delete(0, "end")
            self.entry_fecha.insert(0, self.fecha_seleccionada)
            self._cargar_sesiones()
            top.destroy()

        ctk.CTkButton(top, text="Aceptar", command=confirmar).pack(pady=10)

    def _exportar_a_pdf(self):
        sesiones = SesionController.obtener_sesiones_por_dia(self.fecha_seleccionada)
        if not sesiones:
            CTkMessagebox(title="Advertencia", message="No hay sesiones para exportar.")
            return

        filepath = filedialog.asksaveasfilename(
            defaultextension=".pdf",
            filetypes=[("PDF files", "*.pdf")],
            title="Guardar listado de sesiones"
        )
        if not filepath:
            return

        try:
            doc = SimpleDocTemplate(filepath, pagesize=A4)
            elements = []
            styles = getSampleStyleSheet()

            logo_path = "../Resources/logo.png"
            if os.path.exists(logo_path):
                logo = RLImage(logo_path, width=80, height=80)
                elements.append(logo)
                elements.append(Spacer(1, 12))

            title = Paragraph(f"Registro de sesiones – {self.fecha_seleccionada}", styles['Title'])
            elements.append(title)
            elements.append(Spacer(1, 24))

            data = [["Hora", "Nombre", "Aparato"]]
            clientes = {c.id: c.nombre for c in ClienteController.obtener_todos()}
            aparatos = {a[0]: a[1] for a in AparatoController.obtener_todos_aparatos()}
            for s in sesiones:
                data.append([
                    s.hora_inicio,
                    clientes.get(s.cliente_id, "Desconocido"),
                    aparatos.get(s.aparato_id, "Desconocido")
                ])

            table = Table(data, colWidths=[80, 200, 200])
            table.setStyle(TableStyle([
                ('BACKGROUND', (0, 0), (-1, 0), colors.gray),
                ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
                ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
                ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
                ('FONTSIZE', (0, 0), (-1, -1), 10),
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
            from PIL import Image
            img_path = "../Resources/volver.png"
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