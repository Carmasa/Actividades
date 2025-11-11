# main.py

from tkinter import Tk
from View.login_frame import InterfaceTK
from Model.conection import inicializar_db, insertar_datos_iniciales
from Model.Admin import ADMINS_INICIALES
from Model.Aparato import EQUIPAMIENTO_GIMNASIO


if __name__ == "__main__":
    inicializar_db()
    insertar_datos_iniciales()

    root = Tk()
    app = InterfaceTK(root)
    root.mainloop()