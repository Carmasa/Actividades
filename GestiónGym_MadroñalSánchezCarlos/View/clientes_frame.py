# View/clientes_frame.py
import customtkinter as ctk
from Controller.ClienteController import ClienteController
from Controller.PagoController import PagoController
from tkinter import messagebox
from PIL import Image, ImageTk  # Requiere pip install Pillow

class ClientesFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller
        self.clientes = []
        self.filtros = {
            "estado": "Todos",
            "pago": "Todos"
        }
        self._crear_ui()
        self._cargar_clientes()

    def _crear_ui(self):
        # === Encabezado con logo y título ===
        header = ctk.CTkFrame(self, height=80, fg_color="transparent")
        header.pack(fill="x", padx=20, pady=(20, 0))

        # Logo
        try:
            logo_path = "../Resources/logo.png"
            logo_image = Image.open(logo_path)
            logo_image = logo_image.resize((80, 80), Image.LANCZOS)
            self.logo = ctk.CTkImage(light_image=logo_image, dark_image=logo_image, size=(60, 60))
            ctk.CTkLabel(header, image=self.logo, text="").pack(side="left", padx=(0, 20))
        except Exception as e:
            print(f"⚠️ No se pudo cargar el logo: {e}")
            ctk.CTkLabel(header, text="GYM", font=("Arial", 16)).pack(side="left", padx=(0, 20))

        # Título "Clientes" en grande y centrado
        ctk.CTkLabel(
            header,
            text="Clientes",
            font=("Arial", 32, "bold"),
            anchor="center"
        ).pack(side="left", expand=True)

        # === Barra de acciones ===
        barra_acciones = ctk.CTkFrame(self, height=50, fg_color="gray15")
        barra_acciones.pack(fill="x", padx=20, pady=(10, 10))

        # Botón Añadir (a la izquierda)
        ctk.CTkButton(
            barra_acciones,
            text="👤 Añadir Nuevo Cliente +",
            command=self._abrir_formulario,
            fg_color="#4CAF50",
            hover_color="#45a049",
            width=200
        ).pack(side="left", padx=(0, 20))

        # Filtros y buscador (a la derecha)
        right_frame = ctk.CTkFrame(barra_acciones, fg_color="transparent")
        right_frame.pack(side="right")

        # Estado
        ctk.CTkLabel(right_frame, text="Estado:").pack(side="left", padx=(0, 5))
        self.combo_estado = ctk.CTkComboBox(
            right_frame,
            values=["Todos", "Activo", "Inactivo"],
            width=120,
            command=lambda _: self._aplicar_filtros()
        )
        self.combo_estado.set("Todos")
        self.combo_estado.pack(side="left", padx=(0, 10))

        # Pago
        ctk.CTkLabel(right_frame, text="Pago:").pack(side="left", padx=(0, 5))
        self.combo_pago = ctk.CTkComboBox(
            right_frame,
            values=["Todos", "Pagado", "Pendiente"],
            width=120,
            command=lambda _: self._aplicar_filtros()
        )
        self.combo_pago.set("Todos")
        self.combo_pago.pack(side="left", padx=(0, 10))

        # Buscador
        ctk.CTkLabel(right_frame, text="Buscar:").pack(side="left", padx=(0, 5))
        self.entry_buscar = ctk.CTkEntry(right_frame, width=200, placeholder_text="Nombre...")
        self.entry_buscar.pack(side="left", padx=(0, 10))
        self.entry_buscar.bind("<KeyRelease>", lambda e: self._aplicar_filtros())

        # === Área scrollable ===
        self.scroll_frame = ctk.CTkScrollableFrame(self)
        self.scroll_frame.pack(fill="both", expand=True, padx=20, pady=(0, 20))

        self._crear_encabezado()

    def _crear_encabezado(self):
        encabezado = ctk.CTkFrame(self.scroll_frame, fg_color="gray20")
        encabezado.pack(fill="x", pady=(0, 5))
        cols = ["Nombre", "Email", "Teléfono", "Fecha Registro", "Estado", "Pago"]
        ancho = [180, 200, 120, 120, 100, 100]
        for i, (col, w) in enumerate(zip(cols, ancho)):
            ctk.CTkLabel(encabezado, text=col, font=("Arial", 12, "bold"), width=w).grid(row=0, column=i, padx=2, pady=5)

    def _cargar_clientes(self):
        """Carga todos los clientes y sus estados de pago."""
        self.clientes = ClienteController.obtener_todos()
        self._aplicar_filtros()

    def _aplicar_filtros(self):
        # Limpiar scroll
        for widget in self.scroll_frame.winfo_children():
            if "encabezado" not in str(widget):  # Mantiene el encabezado
                widget.destroy()
        self._crear_encabezado()

        texto_busqueda = self.entry_buscar.get().lower()
        self.filtros["estado"] = self.combo_estado.get()
        self.filtros["pago"] = self.combo_pago.get()

        # Obtener pagos del mes actual para morosidad
        pagos_pendientes = {p.cliente_id for p in PagoController.obtener_pagos_pendientes_mes_actual()}

        for cliente in self.clientes:
            # Filtro por nombre
            if texto_busqueda and not cliente.nombre.lower().startswith(texto_busqueda):
                continue

            # Filtro por estado
            if self.filtros["estado"] != "Todos":
                estado_cliente = "Activo" if cliente.activo else "Inactivo"
                if self.filtros["estado"] != estado_cliente:
                    continue

            # Filtro por pago
            if self.filtros["pago"] != "Todos":
                estado_pago = "Pagado" if cliente.id not in pagos_pendientes else "Pendiente"
                if self.filtros["pago"] != estado_pago:
                    continue

            self._crear_fila_cliente(cliente, pagos_pendientes)

    def _crear_fila_cliente(self, cliente, pagos_pendientes):
        fila = ctk.CTkFrame(self.scroll_frame)
        fila.pack(fill="x", pady=2)

        # Determinar estado de pago
        estado_pago = "Pendiente" if cliente.id in pagos_pendientes else "Pagado"
        color_pago = "red" if estado_pago == "Pendiente" else "green"

        # Datos
        datos = [
            cliente.nombre,
            cliente.email or "-",
            cliente.telefono or "-",
            cliente.fecha_registro or "-",
        ]
        ancho = [180, 200, 120, 120]
        for i, (dato, w) in enumerate(zip(datos, ancho)):
            ctk.CTkLabel(fila, text=dato, width=w).grid(row=0, column=i, padx=2, pady=5)

        # Combobox Estado
        estado_actual = "Activo" if cliente.activo else "Inactivo"
        combo_estado = ctk.CTkComboBox(
            fila,
            values=["Activo", "Inactivo"],
            width=100,
            command=lambda val, c=cliente: self._cambiar_estado(c, val)
        )
        combo_estado.set(estado_actual)
        combo_estado.grid(row=0, column=4, padx=2, pady=5)

        # Combobox Pago
        combo_pago = ctk.CTkComboBox(
            fila,
            values=["Pagado", "Pendiente"],
            width=100,
            text_color=color_pago,
            command=lambda val, c=cliente: self._cambiar_pago(c, val)
        )
        combo_pago.set(estado_pago)
        combo_pago.grid(row=0, column=5, padx=2, pady=5)

    def _cambiar_estado(self, cliente, nuevo_estado):
        print(f"Actualizar estado de {cliente.id} a {nuevo_estado}")
        # TODO: Implementar en ClienteController

    def _cambiar_pago(self, cliente, nuevo_pago):
        print(f"Actualizar pago de {cliente.id} a {nuevo_pago}")
        # TODO: Implementar en PagoController

    def _abrir_formulario(self):
        FormularioCliente(self, self._cliente_guardado)

    def _cliente_guardado(self):
        self._cargar_clientes()


# === Formulario modal ===
class FormularioCliente(ctk.CTkToplevel):
    def __init__(self, parent, callback):
        super().__init__(parent)
        self.callback = callback
        self.title("Añadir Nuevo Cliente")
        self.geometry("500x400")
        self.resizable(False, False)
        self.grab_set()  # Modal

        ctk.CTkLabel(self, text="👤 Añadir Nuevo Cliente", font=("Arial", 16, "bold")).pack(pady=10)

        ctk.CTkLabel(self, text="Nombre *").pack(pady=(10, 0))
        self.entry_nombre = ctk.CTkEntry(self, width=300)
        self.entry_nombre.pack()

        ctk.CTkLabel(self, text="Email").pack(pady=(10, 0))
        self.entry_email = ctk.CTkEntry(self, width=300)
        self.entry_email.pack()

        ctk.CTkLabel(self, text="Teléfono").pack(pady=(10, 0))
        self.entry_telefono = ctk.CTkEntry(self, width=300)
        self.entry_telefono.pack()

        ctk.CTkButton(self, text="Guardar", command=self._guardar, fg_color="#4CAF50").pack(pady=20)

    def _guardar(self):
        nombre = self.entry_nombre.get().strip()
        email = self.entry_email.get().strip()
        telefono = self.entry_telefono.get().strip()

        if not nombre:
            messagebox.showwarning("⚠️ Advertencia", "El nombre es obligatorio.")
            return

        cliente_id = ClienteController.crear_cliente(nombre, email or None, telefono or None)
        if cliente_id:
            messagebox.showinfo("✅ Éxito", "Cliente registrado correctamente.")
            self.callback()
            self.destroy()
        else:
            messagebox.showerror("❌ Error", "No se pudo registrar el cliente.")