# View/clientes_frame.py

import customtkinter as ctk
from Controller.ClienteController import ClienteController
from Controller.PagoController import PagoController
from PIL import Image
from CTkMessagebox import CTkMessagebox

class ClientesFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller
        self.clientes = []
        self.filtros = {"estado": "Todos", "pago": "Todos"}
        self._crear_ui()
        self._cargar_clientes()
        self._crear_boton_volver()

    def _crear_ui(self):
        header = ctk.CTkFrame(self, height=80, fg_color="transparent")
        header.pack(fill="x", padx=20, pady=(20, 0))

        try:
            logo_path = "Resources/logo.png"
            logo_image = Image.open(logo_path)
            logo_image = logo_image.resize((80, 80), Image.LANCZOS)
            self.logo = ctk.CTkImage(light_image=logo_image, dark_image=logo_image, size=(60, 60))
            ctk.CTkLabel(header, image=self.logo, text="").pack(side="left", padx=(0, 20))
        except Exception as e:
            print(f"⚠️ No se pudo cargar el logo: {e}")
            ctk.CTkLabel(header, text="GYM", font=("Arial", 16)).pack(side="left", padx=(0, 20))

        ctk.CTkLabel(header, text="Clientes", font=("Arial", 32, "bold"), anchor="center").pack(side="left", expand=True)

        barra_acciones = ctk.CTkFrame(self, height=50, fg_color="gray15")
        barra_acciones.pack(fill="x", padx=20, pady=(10, 10))

        ctk.CTkButton(
            barra_acciones,
            text="👤 Añadir Nuevo Cliente +",
            command=self._abrir_formulario,
            fg_color="#4CAF50",
            hover_color="#45a049",
            width=200
        ).pack(side="left", padx=(0, 20))

        right_frame = ctk.CTkFrame(barra_acciones, fg_color="transparent")
        right_frame.pack(side="right")

        ctk.CTkLabel(right_frame, text="Estado:").pack(side="left", padx=(0, 5))
        self.combo_estado = ctk.CTkComboBox(
            right_frame,
            values=["Todos", "Activo", "Inactivo"],
            width=120,
            command=lambda _: self._aplicar_filtros()
        )
        self.combo_estado.set("Todos")
        self.combo_estado.pack(side="left", padx=(0, 10))

        ctk.CTkLabel(right_frame, text="Pago:").pack(side="left", padx=(0, 5))
        self.combo_pago = ctk.CTkComboBox(
            right_frame,
            values=["Todos", "Pagado", "Pendiente"],
            width=120,
            command=lambda _: self._aplicar_filtros()
        )
        self.combo_pago.set("Todos")
        self.combo_pago.pack(side="left", padx=(0, 10))

        ctk.CTkLabel(right_frame, text="Buscar:").pack(side="left", padx=(0, 5))
        self.entry_buscar = ctk.CTkEntry(right_frame, width=200, placeholder_text="Nombre...")
        self.entry_buscar.pack(side="left", padx=(0, 10))
        self.entry_buscar.bind("<KeyRelease>", lambda e: self._aplicar_filtros())

        self.scroll_frame = ctk.CTkScrollableFrame(self)
        self.scroll_frame.pack(fill="both", expand=True, padx=20, pady=(0, 20))

        self._crear_encabezado()

    def _crear_encabezado(self):
        encabezado = ctk.CTkFrame(self.scroll_frame, fg_color="gray20")
        encabezado.pack(fill="x", pady=(0, 5))
        cols = ["Nombre", "Email", "Teléfono", "Fecha Registro", "Estado", "Pago", "Acción"]
        ancho = [180, 200, 120, 120, 100, 100, 100]  # +1 columna
        for i, (col, w) in enumerate(zip(cols, ancho)):
            ctk.CTkLabel(encabezado, text=col, font=("Arial", 12, "bold"), width=w).grid(row=0, column=i, padx=2, pady=5)


    def _cargar_clientes(self):
        self.clientes = ClienteController.obtener_todos()
        self._aplicar_filtros()

    def _aplicar_filtros(self):
        for widget in self.scroll_frame.winfo_children():
            if "encabezado" not in str(widget):
                widget.destroy()
        self._crear_encabezado()

        texto_busqueda = self.entry_buscar.get().lower()
        self.filtros["estado"] = self.combo_estado.get()
        self.filtros["pago"] = self.combo_pago.get()

        pagos_pendientes = {p.cliente_id for p in PagoController.obtener_pagos_pendientes_mes_actual()}

        for cliente in self.clientes:
            if texto_busqueda and not cliente.nombre.lower().startswith(texto_busqueda):
                continue
            if self.filtros["estado"] != "Todos":
                estado_cliente = "Activo" if cliente.activo else "Inactivo"
                if self.filtros["estado"] != estado_cliente:
                    continue
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

        # Datos básicos
        datos = [
            cliente.nombre,
            cliente.email or "-",
            cliente.telefono or "-",
            cliente.fecha_registro or "-",
        ]
        ancho = [180, 200, 120, 120]
        for i, (dato, w) in enumerate(zip(datos, ancho)):
            ctk.CTkLabel(fila, text=dato, width=w).grid(row=0, column=i, padx=2, pady=5)

        # Estado
        estado_actual = "Activo" if cliente.activo else "Inactivo"
        color_estado = "green" if cliente.activo else "red"
        ctk.CTkLabel(fila, text=estado_actual, width=100, text_color=color_estado).grid(row=0, column=4, padx=2, pady=5)

        # Pago
        ctk.CTkLabel(fila, text=estado_pago, width=100, text_color=color_pago).grid(row=0, column=5, padx=2, pady=5)

        # Botón Eliminar
        ctk.CTkButton(
            fila,
            text="Eliminar cliente",
            width=100,
            fg_color="red",
            hover_color="#8B0000",
            command=lambda cid=cliente.id, nombre=cliente.nombre: self._confirmar_eliminacion(cid, nombre)
        ).grid(row=0, column=6, padx=20, pady=5)

    def _confirmar_eliminacion(self, cliente_id, nombre_cliente):
        # Crear el mensaje de confirmación
        msg = CTkMessagebox(
            title="Confirmar eliminación",
            message=f"¿Eliminar cliente '{nombre_cliente}' y todos sus registros?",
            icon="warning",
            option_1="Cancelar",
            option_2="Eliminar"
        )
        # Esperar la respuesta del usuario
        respuesta = msg.get()
        if respuesta == "Eliminar":
            self._eliminar_cliente(cliente_id)


    def _eliminar_cliente(self, cliente_id):
        if ClienteController.eliminar_cliente(cliente_id):
            CTkMessagebox(title="Éxito", message="Cliente eliminado correctamente.")
            self._cargar_clientes()  # Recargar lista
        else:
            CTkMessagebox(title="Error", message="No se pudo eliminar el cliente.")


    def _abrir_formulario(self):
        FormularioCliente(self, self._cliente_guardado)

    def _cliente_guardado(self):
        self._cargar_clientes()

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


# === Formulario modal ===
class FormularioCliente(ctk.CTkToplevel):
    def __init__(self, parent, callback):
        super().__init__(parent)
        self.callback = callback
        self.title("Añadir Nuevo Cliente")
        self.geometry("500x400")
        self.resizable(False, False)
        self.grab_set()

        self.update_idletasks()
        parent_x = parent.winfo_rootx()
        parent_y = parent.winfo_rooty()
        parent_width = parent.winfo_width()
        parent_height = parent.winfo_height()
        my_width = self.winfo_width()
        my_height = self.winfo_height()
        x = parent_x + (parent_width // 2) - (my_width // 2)
        y = parent_y + (parent_height // 2) - (my_height // 2)
        self.geometry(f"{my_width}x{my_height}+{x}+{y}")

        ctk.CTkLabel(self, text="👤 Añadir Nuevo Cliente", font=("Arial", 16, "bold")).pack(pady=10)

        ctk.CTkLabel(self, text="Nombre *").pack(pady=(10, 0))
        self.entry_nombre = ctk.CTkEntry(self, width=300, takefocus=False)
        self.entry_nombre.pack()

        ctk.CTkLabel(self, text="Email").pack(pady=(10, 0))
        self.entry_email = ctk.CTkEntry(self, width=300, takefocus=False)
        self.entry_email.pack()

        ctk.CTkLabel(self, text="Teléfono").pack(pady=(10, 0))
        self.entry_telefono = ctk.CTkEntry(self, width=300, takefocus=False)
        self.entry_telefono.pack()

        ctk.CTkButton(self, text="Guardar", command=self._guardar, fg_color="#4CAF50").pack(pady=20)

        self.after(100, lambda: self.focus_set())

    def _guardar(self):
        nombre = self.entry_nombre.get().strip()
        email = self.entry_email.get().strip()
        telefono = self.entry_telefono.get().strip()

        if not nombre:
            CTkMessagebox(title="Advertencia", message="El nombre es obligatorio.")
            return

        cliente_id = ClienteController.crear_cliente(nombre, email or None, telefono or None)
        if cliente_id:
            CTkMessagebox(title="Éxito", message="Cliente registrado correctamente.", icon="check")
            self.after(300, self.destroy)
            self.callback()
        else:
            CTkMessagebox(title="Error", message="No se pudo registrar el cliente.", icon="cancel")