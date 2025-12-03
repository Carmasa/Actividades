from Controller.init_db import inicializar_datos
from View.app import GymApp

if __name__ == "__main__":
    inicializar_datos()
    app = GymApp()
    app.mainloop()