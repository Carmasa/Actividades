# main.py

from tkinter import Tk
from View.JuegoTK import JuegoTK
from Model.conection import inicializar_db, insertar_peliculas_iniciales
from Model.Pelicula import BIBLIOTECA

if __name__ == "__main__":
    inicializar_db()
    insertar_peliculas_iniciales(BIBLIOTECA)

    root = Tk()
    app = JuegoTK(root)
    root.mainloop()