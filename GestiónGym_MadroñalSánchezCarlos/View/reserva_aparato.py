# View/reserva_aparato.py

import customtkinter as ctk
from Controller.SesionController import SesionController
from Controller.ClienteController import ClienteController
from datetime import datetime, timedelta
from tkcalendar import Calendar
from CTkMessagebox import CTkMessagebox

class ReservaAparato(ctk.CTkToplevel):
    def __init__(self, parent, aparato, callback):
        super().__init__(parent)
        self.parent = parent
        self.aparato = aparato
        self.callback = callback
        self.title(f"{aparato[1]}")
        self.geometry("400x550")
        self.resizable(False, False)
        self.grab_set()

        # Centrar ventana
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

        ctk.CTkLabel(self, text=f"{aparato[1]}", font=("Arial", 18, "bold")).pack(pady=10)

        ctk.CTkLabel(self, text="Fecha:").pack(pady=(10, 0))
        self.fecha_seleccionada = datetime.now().strftime("%Y-%m-%d")
        self.label_fecha = ctk.CTkLabel(self, text=self.fecha_seleccionada, fg_color="gray30", corner_radius=6, width=200)
        self.label_fecha.pack(pady=5)

        ctk.CTkButton(self, text="📅 Seleccionar fecha", command=self._abrir_calendario, width=200).pack(pady=5)

        ctk.CTkLabel(self, text="Turno:").pack(pady=(10, 0))
        self.turno_var = ctk.StringVar(value="mañana")
        frame_turnos = ctk.CTkFrame(self, fg_color="transparent")
        frame_turnos.pack(pady=5)

        ctk.CTkRadioButton(frame_turnos, text="Mañana (6-14)", variable=self.turno_var, value="mañana", command=self._actualizar_horas).pack(side="left", padx=5)
        ctk.CTkRadioButton(frame_turnos, text="Tarde (14-22)", variable=self.turno_var, value="tarde", command=self._actualizar_horas).pack(side="left", padx=5)
        ctk.CTkRadioButton(frame_turnos, text="Noche (22-6)", variable=self.turno_var, value="noche", command=self._actualizar_horas).pack(side="left", padx=5)

        ctk.CTkLabel(self, text="Hora:").pack(pady=(10, 0))
        self.combo_hora = ctk.CTkComboBox(self, values=["Cargando..."], width=200)
        self.combo_hora.pack(pady=5)

        ctk.CTkLabel(self, text="Nombre:").pack(pady=(10, 0))
        self.entry_cliente = ctk.CTkEntry(self, width=200, placeholder_text="Buscar cliente...")
        self.entry_cliente.pack(pady=5)
        self.entry_cliente.bind("<KeyRelease>", self._buscar_clientes)

        self.lista_sugerencias = ctk.CTkFrame(self, fg_color="gray20", width=200)
        self.lista_sugerencias.pack_forget()

        ctk.CTkButton(self, text="➕ Añadir Sesión +", command=self._crear_sesion, fg_color="#4CAF50", hover_color="#45a049").pack(pady=20)

        self._cargar_horas_disponibles()

    def _cargar_horas_disponibles(self):
        turno = self.turno_var.get()
        horas = []

        if turno == "mañana":
            start_hour = 6
            end_hour = 14
        elif turno == "tarde":
            start_hour = 14
            end_hour = 22
        else:
            start_hour = 22
            end_hour = 6

        current = start_hour
        while True:
            for minuto in [0, 30]:
                hora_str = f"{current:02d}:{minuto:02d}"
                if not SesionController.existe_sesion_en_horario(self.aparato[0], self.fecha_seleccionada, hora_str):
                    horas.append(hora_str)
            if turno == "noche" and current == 23:
                for h in range(0, 6):
                    for m in [0, 30]:
                        hora_str = f"{h:02d}:{m:02d}"
                        if not SesionController.existe_sesion_en_horario(self.aparato[0], self.fecha_seleccionada, hora_str):
                            horas.append(hora_str)
                break
            current += 1
            if (turno != "noche" and current >= end_hour) or (turno == "noche" and current >= 24):
                break

        if not horas:
            horas = ["No hay horarios disponibles"]

        self.combo_hora.configure(values=horas)
        if horas:
            self.combo_hora.set(horas[0])

    def _actualizar_horas(self):
        self._cargar_horas_disponibles()

    def _buscar_clientes(self, event=None):
        texto = self.entry_cliente.get().strip().lower()
        if not texto:
            self.lista_sugerencias.pack_forget()
            return

        clientes = ClienteController.obtener_todos()
        sugerencias = [c.nombre for c in clientes if texto in c.nombre.lower()]

        for widget in self.lista_sugerencias.winfo_children():
            widget.destroy()

        for nombre in sugerencias[:5]:
            btn = ctk.CTkButton(
                self.lista_sugerencias,
                text=nombre,
                command=lambda n=nombre: self._seleccionar_cliente(n),
                fg_color="gray30",
                hover_color="gray40",
                width=180,
                anchor="w"
            )
            btn.pack(fill="x", padx=5, pady=2)

        if sugerencias:
            self.lista_sugerencias.pack(pady=5)
        else:
            self.lista_sugerencias.pack_forget()

    def _seleccionar_cliente(self, nombre):
        self.entry_cliente.delete(0, "end")
        self.entry_cliente.insert(0, nombre)
        self.lista_sugerencias.pack_forget()

    def _crear_sesion(self):
        fecha = self.fecha_seleccionada
        hora = self.combo_hora.get()
        nombre_cliente = self.entry_cliente.get().strip()

        if not fecha or not hora or not nombre_cliente:
            CTkMessagebox(title="Error", message="Completa todos los campos.")
            return

        clientes = ClienteController.obtener_todos()
        cliente = next((c for c in clientes if c.nombre == nombre_cliente), None)
        if not cliente:
            CTkMessagebox(title="Error", message="Cliente no encontrado.")
            return

        sesion_id = SesionController.crear_sesion(cliente.id, self.aparato[0], fecha, hora)
        if sesion_id:
            CTkMessagebox(title="Éxito", message="Sesión creada correctamente.", icon="check")
            self.focus_set()
            self.after(300, self.destroy)
            self.callback()
        else:
            CTkMessagebox(title="Error", message="No se pudo crear la sesión.", icon="cancel")

    def _abrir_calendario(self):
        top = ctk.CTkToplevel(self)
        top.title("Seleccionar fecha")
        top.geometry("300x300")
        top.grab_set()

        cal = Calendar(top, selectmode="day", year=datetime.now().year, month=datetime.now().month, day=datetime.now().day, date_pattern="y-mm-dd")
        cal.pack(pady=10)

        def confirmar_fecha():
            self.fecha_seleccionada = cal.get_date()
            self.label_fecha.configure(text=self.fecha_seleccionada)
            self._cargar_horas_disponibles()
            top.destroy()

        ctk.CTkButton(top, text="Aceptar", command=confirmar_fecha).pack(pady=10)