# Controller/init_db.py

from Model.conection import inicializar_db
from Model.Admin import ADMINS_INICIALES
from Model.Aparato import EQUIPAMIENTO_GIMNASIO
from Controller.AdminController import AdminController
from Controller.AparatoController import AparatoController


def inicializar_datos():
    inicializar_db()

    # Insertar admins si no existen
    if not AdminController.existen_admins():
        AdminController.insertar_admins_iniciales(ADMINS_INICIALES)

    # Insertar aparatos si no existen
    if not AparatoController.existen_aparatos():
        AparatoController.insertar_aparatos_iniciales(EQUIPAMIENTO_GIMNASIO)