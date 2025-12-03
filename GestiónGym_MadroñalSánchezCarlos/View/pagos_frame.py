# View/pagos_frame.py

import customtkinter as ctk
from Controller.ClienteController import ClienteController
from Controller.PagoController import PagoController
from datetime import datetime
from PIL import Image
from CTkMessagebox import CTkMessagebox
from Model.Pago import Pago

class PagosFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller
        self.cliente_seleccionado = None
        self.pago_actual = None
        self._crear_ui()
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
        except:
            pass

        ctk.CTkLabel(header, text="Gestión de Pagos", font=("Arial", 32, "bold")).pack(side="left", expand=True)

        barra_busqueda = ctk.CTkFrame(self, height=50, fg_color="gray15")
        barra_busqueda.pack(fill="x", padx=20, pady=10)

        ctk.CTkLabel(barra_busqueda, text="Buscar cliente:").pack(side="left", padx=(0, 10))
        self.entry_buscar = ctk.CTkEntry(barra_busqueda, width=300, placeholder_text="Nombre...")
        self.entry_buscar.pack(side="left", padx=(0, 10))
        self.entry_buscar.bind("<KeyRelease>", self._buscar_cliente)

        self.panel_detalles = ctk.CTkFrame(self, fg_color="transparent")
        self.panel_detalles.pack(expand=True, fill="both", padx=20, pady=20)

        self.label_info = ctk.CTkLabel(self.panel_detalles, text="Busca un cliente para gestionar su pago.", font=("Arial", 14))
        self.label_info.pack(pady=20)

    def _buscar_cliente(self, event=None):
        nombre = self.entry_buscar.get().strip().lower()
        if not nombre:
            self._limpiar_panel()
            return

        clientes = ClienteController.obtener_todos()
        candidato = None
        for c in clientes:
            if nombre in c.nombre.lower():
                candidato = c
                break

        if candidato:
            self._mostrar_pago(candidato)
        else:
            self._limpiar_panel()
            self.label_info.configure(text="Cliente no encontrado.")

    def _mostrar_pago(self, cliente):
        for widget in self.panel_detalles.winfo_children():
            widget.destroy()

        mes_actual = datetime.now().strftime("%Y-%m")
        todos_pagos_mes = PagoController.obtener_todos_los_pagos_del_mes(mes_actual)
        pago = next((p for p in todos_pagos_mes if p.cliente_id == cliente.id), None)

        if not pago:
            pago_id = PagoController.registrar_pago_mensual(cliente.id, mes_actual, 50.0)
            todos_pagos_mes = PagoController.obtener_todos_los_pagos_del_mes(mes_actual)
            pago = next((p for p in todos_pagos_mes if p.cliente_id == cliente.id), None)

        self.cliente_seleccionado = cliente
        self.pago_actual = pago

        ctk.CTkLabel(self.panel_detalles, text=f"Cliente: {cliente.nombre}", font=("Arial", 16, "bold")).pack(pady=5)
        ctk.CTkLabel(self.panel_detalles, text=f"Email: {cliente.email or '-'}").pack(pady=2)
        ctk.CTkLabel(self.panel_detalles, text=f"Teléfono: {cliente.telefono or '-'}").pack(pady=2)
        ctk.CTkLabel(self.panel_detalles, text=f"Monto: {pago.monto} €", font=("Arial", 14)).pack(pady=(20, 5))

        estado = pago.estado
        if estado == "pendiente":
            btn_text = "✅ Marcar como pagado"
            btn_color = "green"
            btn_command = self._marcar_pagado
        else:
            btn_text = "🔄 Reabrir pago"
            btn_color = "orange"
            btn_command = self._reabrir_pago

        ctk.CTkButton(self.panel_detalles, text=btn_text, fg_color=btn_color, command=btn_command).pack(pady=20)

    def _marcar_pagado(self):
        if self.pago_actual and self.cliente_seleccionado:
            if PagoController.marcar_como_pagado(self.pago_actual.id):
                CTkMessagebox(title="Éxito", message="Pago registrado correctamente.")
                self._mostrar_pago(self.cliente_seleccionado)
            else:
                CTkMessagebox(title="Error", message="No se pudo actualizar el pago.")

    def _reabrir_pago(self):
        if self.pago_actual and self.cliente_seleccionado:
            try:
                with PagoController.obtener_conexion() as conn:
                    cursor = conn.cursor()
                    cursor.execute("UPDATE Pago SET estado = 'pendiente', fecha_pago = NULL WHERE id = ?", (self.pago_actual.id,))
                    conn.commit()
                CTkMessagebox(title="Éxito", message="Pago reabierto.")
                self._mostrar_pago(self.cliente_seleccionado)
            except Exception as e:
                CTkMessagebox(title="Error", message="No se pudo reabrir el pago.")

    def _limpiar_panel(self):
        for widget in self.panel_detalles.winfo_children():
            widget.destroy()
        self.label_info = ctk.CTkLabel(self.panel_detalles, text="Busca un cliente para gestionar su pago.", font=("Arial", 14))
        self.label_info.pack(pady=20)

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