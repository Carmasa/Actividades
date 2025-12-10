# View/pagos_frame.py

import customtkinter as ctk
from Controller.PagoController import PagoController
from datetime import datetime
from PIL import Image
from CTkMessagebox import CTkMessagebox

class PagosFrame(ctk.CTkFrame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller
        self.mes_actual = datetime.now().strftime("%Y-%m")
        self._crear_ui()
        self._cargar_morosos()
        self._crear_boton_volver()

    def _crear_ui(self):
        # === Encabezado ===
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

        # === Selector de mes ===
        barra_mes = ctk.CTkFrame(self, height=50, fg_color="gray15")
        barra_mes.pack(fill="x", padx=20, pady=10)

        ctk.CTkLabel(barra_mes, text="Mes:").pack(side="left", padx=(0, 10))
        self.mes_var = ctk.StringVar(value=self.mes_actual)
        self.combo_mes = ctk.CTkComboBox(
            barra_mes,
            values=self._generar_meses(),
            width=120,
            variable=self.mes_var,
            command=self._cambiar_mes
        )
        self.combo_mes.pack(side="left", padx=(0, 20))

        # === Área scrollable ===
        self.scroll_frame = ctk.CTkScrollableFrame(self)
        self.scroll_frame.pack(fill="both", expand=True, padx=20, pady=(0, 20))

        self._crear_encabezado()

    def _generar_meses(self):
        """Genera una lista de meses desde hace 6 meses hasta dentro de 6 meses."""
        meses = []
        hoy = datetime.now()
        for i in range(-6, 7):
            mes = hoy.replace(day=1)  # Primer día del mes actual
            if i < 0:
                mes = mes.replace(month=mes.month + i) if mes.month + i > 0 else mes.replace(year=mes.year - 1, month=12 + mes.month + i)
            elif i > 0:
                mes = mes.replace(month=mes.month + i) if mes.month + i <= 12 else mes.replace(year=mes.year + 1, month=mes.month + i - 12)
            meses.append(mes.strftime("%Y-%m"))
        return meses

    def _crear_encabezado(self):
        encabezado = ctk.CTkFrame(self.scroll_frame, fg_color="gray20")
        encabezado.pack(fill="x", pady=(0, 5))
        cols = ["Nombre", "Email", "Teléfono", "Monto", "Acción"]
        ancho = [200, 200, 150, 100, 100]
        for i, (col, w) in enumerate(zip(cols, ancho)):
            ctk.CTkLabel(encabezado, text=col, font=("Arial", 12, "bold"), width=w).grid(row=0, column=i, padx=2, pady=5)

    def _cargar_morosos(self):
        mes = self.mes_var.get()
        self.morosos = PagoController.obtener_morosos_por_mes(mes)

        # Limpiar
        for widget in self.scroll_frame.winfo_children():
            widget.destroy()
        self._crear_encabezado()

        if not self.morosos:
            ctk.CTkLabel(self.scroll_frame, text="No hay clientes con pago pendiente.", font=("Arial", 14)).pack(pady=20)
            return

        for moroso in self.morosos:
            cliente_id, nombre, email, telefono, fecha_registro, pago_id, monto = moroso
            fila = ctk.CTkFrame(self.scroll_frame)
            fila.pack(fill="x", pady=2)

            # Datos
            datos = [nombre, email or "-", telefono or "-", f"{monto} €"]
            ancho = [200, 200, 150, 100]
            for i, (dato, w) in enumerate(zip(datos, ancho)):
                ctk.CTkLabel(fila, text=dato, width=w).grid(row=0, column=i, padx=2, pady=5)

            # Botón Pagar
            ctk.CTkButton(
                fila,
                text="💰 Pagar",
                width=100,
                fg_color="green",
                hover_color="#2E8B57",
                command=lambda c=cliente_id, p=pago_id, m=monto, n=nombre: self._abrir_pago(c, p, m, n)
            ).grid(row=0, column=4, padx=2, pady=5)

    def _cambiar_mes(self, mes_seleccionado):
        self._cargar_morosos()

    def _abrir_pago(self, cliente_id, pago_id, monto_actual, nombre_cliente):
        VentanaPago(self, pago_id, monto_actual, nombre_cliente, self._pago_realizado)

    def _pago_realizado(self):
        self._cargar_morosos()

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


# === Ventana modal para registrar pago ===
class VentanaPago(ctk.CTkToplevel):
    def __init__(self, parent, pago_id, monto_actual, nombre_cliente, callback):
        super().__init__(parent)
        self.pago_id = pago_id
        self.callback = callback
        self.title(f"Pago - {nombre_cliente}")
        self.geometry("400x250")
        self.resizable(False, False)
        self.grab_set()

        # Centrar
        self.update_idletasks()
        x = parent.winfo_rootx() + (parent.winfo_width() // 2) - (self.winfo_width() // 2)
        y = parent.winfo_rooty() + (parent.winfo_height() // 2) - (self.winfo_height() // 2)
        self.geometry(f"+{x}+{y}")

        ctk.CTkLabel(self, text=f"Registrar pago para:\n{nombre_cliente}", font=("Arial", 14, "bold")).pack(pady=10)

        ctk.CTkLabel(self, text="Monto (€):").pack(pady=(10, 0))
        self.entry_monto = ctk.CTkEntry(self, width=200, takefocus=False)
        self.entry_monto.insert(0, str(monto_actual))
        self.entry_monto.pack(pady=5)

        ctk.CTkButton(
            self,
            text="✅ Confirmar Pago",
            fg_color="green",
            hover_color="#2E8B57",
            command=self._confirmar_pago
        ).pack(pady=20)

    def tkraise(self, aboveThis=None):
        self.mes_actual = datetime.now().strftime("%Y-%m")
        self.mes_var.set(self.mes_actual)
        self._cargar_morosos()
        super().tkraise(aboveThis)

    def _confirmar_pago(self):
        try:
            monto = float(self.entry_monto.get())
            if monto <= 0:
                CTkMessagebox(title="Error", message="El monto debe ser mayor que 0.")
                return
        except ValueError:
            CTkMessagebox(title="Error", message="Introduce un monto válido.")
            return

        if PagoController.marcar_como_pagado(self.pago_id):
            CTkMessagebox(title="Éxito", message="Pago registrado correctamente.")
            self.callback()
            self.after(100, self.destroy)
        else:
            CTkMessagebox(title="Error", message="No se pudo registrar el pago.")