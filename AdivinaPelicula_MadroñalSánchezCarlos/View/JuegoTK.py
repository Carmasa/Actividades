# interfaz.py

import tkinter as tk
from tkinter import messagebox, simpledialog, font
from Model.conection import insertar_puntuacion, obtener_pelicula_aleatoria, leer_puntuaciones
import unicodedata


def normalizar_texto(texto):
    """Elimina tildes y normaliza texto para comparación."""
    texto_sin_tildes = ''.join(
        c for c in unicodedata.normalize('NFD', texto)
        if unicodedata.category(c) != 'Mn'
    )
    return texto_sin_tildes.strip().lower()


class JuegoTK:
    def __init__(self, root):
        self.root = root
        self.root.title("🎬 Adivina la Película")
        self.root.geometry("650x550")
        self.root.resizable(False, False)
        self.root.configure(bg="#1e1e2e")  # Fondo oscuro tipo cine

        # Estilos
        self.fuente_titulo = font.Font(family="Helvetica", size=18, weight="bold")
        self.fuente_subtitulo = font.Font(family="Helvetica", size=12, weight="bold")
        self.fuente_texto = font.Font(family="Helvetica", size=11)
        self.fuente_pistas = font.Font(family="Helvetica", size=10)

        self.pelicula_actual = None
        self.pista_index = 0
        self.puntos = 35
        self.nombre_jugador = ""

        self.solicitar_nombre()
        if not self.nombre_jugador:
            self.nombre_jugador = "Anónimo"

        self.crear_widgets()
        self.nueva_partida()

    def solicitar_nombre(self):
        self.nombre_jugador = simpledialog.askstring(
            "👤 Nombre del Jugador",
            "Ingresa tu nombre:",
            parent=self.root
        )
        if self.nombre_jugador is None:
            self.root.quit()

    def crear_widgets(self):
        # Título principal
        titulo = tk.Label(
            self.root,
            text="🎬 Adivina la Película",
            font=self.fuente_titulo,
            fg="#ffd700",  # Dorado
            bg="#1e1e2e"
        )
        titulo.pack(pady=(20, 10))

        # Puntos
        self.label_puntos = tk.Label(
            self.root,
            text=f"⭐ Puntos: {self.puntos}",
            font=self.fuente_subtitulo,
            fg="#4ade80",  # Verde suave
            bg="#1e1e2e"
        )
        self.label_puntos.pack(pady=(0, 15))

        # Marco para pistas (con borde simulado)
        frame_pistas = tk.Frame(self.root, bg="#2d2d44", relief="flat", bd=0)
        frame_pistas.pack(padx=30, pady=(0, 15), fill="both", expand=False)

        label_pistas = tk.Label(
            frame_pistas,
            text="🔍 Pistas:",
            font=self.fuente_subtitulo,
            fg="#89b4fa",  # Azul claro
            bg="#2d2d44"
        )
        label_pistas.pack(anchor="w", padx=10, pady=(10, 5))

        self.text_pistas = tk.Text(
            frame_pistas,
            height=7,
            width=70,
            state="disabled",
            wrap="word",
            font=self.fuente_pistas,
            bg="#3b3b5a",
            fg="#e0e0ff",
            relief="flat",
            padx=10,
            pady=10
        )
        self.text_pistas.pack(padx=10, pady=(0, 10))

        # Entrada de respuesta
        label_respuesta = tk.Label(
            self.root,
            text="❓ ¿Cuál es la película?",
            font=self.fuente_subtitulo,
            fg="#f87171",  # Rojo suave
            bg="#1e1e2e"
        )
        label_respuesta.pack(pady=(10, 5))

        self.entrada_adivinar = tk.Entry(
            self.root,
            width=40,
            font=self.fuente_texto,
            justify="center",
            bg="#3b3b5a",
            fg="#ffffff",
            insertbackground="#ffd700",
            relief="flat"
        )
        self.entrada_adivinar.pack(pady=5)
        self.entrada_adivinar.bind("<Return>", self.intentar_adivinar)

        # Botones
        frame_botones = tk.Frame(self.root, bg="#1e1e2e")
        frame_botones.pack(pady=20)

        # Estilo común para botones
        estilo_btn = {
            "font": ("Helvetica", 10, "bold"),
            "fg": "white",
            "width": 13,
            "height": 1,
            "relief": "flat",
            "bd": 0
        }

        tk.Button(
            frame_botones, text="💡 Pedir Pista",
            command=self.pedir_pista,
            bg="#4338ca",  # Índigo
            **estilo_btn
        ).pack(side="left", padx=6)

        tk.Button(
            frame_botones, text="✅ Adivinar",
            command=self.intentar_adivinar,
            bg="#0d9488",  # Verde esmeralda
            **estilo_btn
        ).pack(side="left", padx=6)

        tk.Button(
            frame_botones, text="🔄 Nueva Partida",
            command=self.nueva_partida,
            bg="#7c3aed",  # Violeta
            **estilo_btn
        ).pack(side="left", padx=6)

        tk.Button(
            frame_botones, text="🏆 Ranking",
            command=self.mostrar_ranking,
            bg="#ec4899",  # Rosa
            **estilo_btn
        ).pack(side="left", padx=6)

    def nueva_partida(self):
        peli = obtener_pelicula_aleatoria()
        if not peli:
            messagebox.showerror("Error", "No hay películas en la base de datos.")
            self.root.quit()
            return

        self.pelicula_actual = peli
        self.pista_index = 0
        self.puntos = 35
        self.label_puntos.config(text=f"⭐ Puntos: {self.puntos}")
        self.text_pistas.config(state="normal")
        self.text_pistas.delete(1.0, tk.END)
        self.text_pistas.config(state="disabled")
        self.entrada_adivinar.delete(0, tk.END)

        # Mostrar primera pista automáticamente
        if peli["pistas"]:
            primera_pista = peli["pistas"][0]
            self.text_pistas.config(state="normal")
            self.text_pistas.insert(tk.END, f"Pista 1:  {primera_pista}\n")
            self.text_pistas.config(state="disabled")
            self.pista_index = 1
            self.puntos -= 5
            self.label_puntos.config(text=f"⭐ Puntos: {self.puntos}")

    def pedir_pista(self):
        if self.pista_index >= len(self.pelicula_actual["pistas"]):
            messagebox.showinfo("Pistas", "¡No quedan más pistas!")
            return
        if self.puntos <= 0:
            messagebox.showwarning("Sin puntos", "No tienes puntos suficientes.")
            return

        nueva_pista = self.pelicula_actual["pistas"][self.pista_index]
        self.text_pistas.config(state="normal")
        self.text_pistas.insert(tk.END, f"Pista {self.pista_index + 1}: {nueva_pista}\n")
        self.text_pistas.config(state="disabled")

        self.pista_index += 1
        self.puntos -= 5
        self.label_puntos.config(text=f"Puntos: {self.puntos}")

        if self.puntos <= 0:
            self.finalizar_juego(False)

    def mostrar_ranking(self):
        puntuaciones = leer_puntuaciones()
        if not puntuaciones:
            messagebox.showinfo("🏆 Ranking", "Aún no hay puntuaciones.")
            return

        ranking_win = tk.Toplevel(self.root)
        ranking_win.title("🏆 Top 10 - Ranking")
        ranking_win.geometry("420x450")
        ranking_win.resizable(False, False)
        ranking_win.configure(bg="#1e1e2e")

        tk.Label(
            ranking_win,
            text="🏆 TOP 10 - Mejores Puntuaciones",
            font=("Helvetica", 14, "bold"),
            fg="#ffd700",
            bg="#1e1e2e"
        ).pack(pady=15)

        # Marco con scroll
        frame = tk.Frame(ranking_win, bg="#2d2d44")
        frame.pack(fill="both", expand=True, padx=20, pady=(0, 15))

        canvas = tk.Canvas(frame, bg="#2d2d44", highlightthickness=0)
        scrollbar = tk.Scrollbar(frame, orient="vertical", command=canvas.yview)
        scrollable_frame = tk.Frame(canvas, bg="#2d2d44")

        scrollable_frame.bind(
            "<Configure>",
            lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        )

        canvas.create_window((0, 0), window=scrollable_frame, anchor="nw")
        canvas.configure(yscrollcommand=scrollbar.set)

        # Colores por posición
        colores = ["#ffd700", "#c0c0c0", "#cd7f32", "#4ade80", "#4ade80"] + ["#89b4fa"] * 6

        for i, (id_, nombre, pts) in enumerate(puntuaciones[:10], 1):
            color = colores[i - 1] if i <= len(colores) else "#89b4fa"
            texto = f"{'🥇' if i == 1 else '🥈' if i == 2 else '🥉' if i == 3 else f'{i}.'} {nombre[:15]} — {pts} pts"
            tk.Label(
                scrollable_frame,
                text=texto,
                font=("Helvetica", 11),
                fg=color,
                bg="#2d2d44",
                anchor="w"
            ).pack(fill="x", padx=10, pady=4)

        canvas.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")

        tk.Button(
            ranking_win,
            text="Cerrar",
            command=ranking_win.destroy,
            bg="#4338ca",
            fg="white",
            font=("Helvetica", 10, "bold"),
            width=10,
            relief="flat"
        ).pack(pady=(0, 15))

    def intentar_adivinar(self, event=None):
        intento = self.entrada_adivinar.get().strip()
        if not intento:
            messagebox.showwarning("⚠️ Advertencia", "Escribe una respuesta.")
            return

        if normalizar_texto(intento) == normalizar_texto(self.pelicula_actual["nombre"]):
            messagebox.showinfo(
                "🎉 ¡Correcto!",
                f"¡Felicidades, {self.nombre_jugador}!\nLa película era:\n\n🎬 {self.pelicula_actual['nombre']}"
            )
            self.finalizar_juego(True)
        else:
            messagebox.showerror("❌ Incorrecto", "¡No es correcto! Sigue intentando.")

    def finalizar_juego(self, acertado):
        puntos_finales = self.puntos if acertado else 0
        insertar_puntuacion(self.nombre_jugador, puntos_finales)

        msg = f"{'¡Has ganado!' if acertado else '¡Casi lo logras!'}\nTu puntuación final: {puntos_finales}"
        messagebox.showinfo("🎬 Fin del Juego", msg)

        if messagebox.askyesno("¿Otra partida?", "¿Quieres jugar de nuevo?"):
            self.nueva_partida()
        else:
            self.root.quit()