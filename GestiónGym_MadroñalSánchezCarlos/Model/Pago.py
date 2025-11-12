# Model/Pago.py

class Pago:
    def __init__(self, id=None, cliente_id=None, mes=None, monto=0.0, fecha_pago=None, estado="pendiente"):
        self.id = id
        self.cliente_id = cliente_id
        self.mes = mes  # Formato: 'YYYY-MM'
        self.monto = monto
        self.fecha_pago = fecha_pago  # Formato: 'YYYY-MM-DD' o None
        self.estado = estado  # 'pendiente', 'pagado'

    def __repr__(self):
        return f"Pago(id={self.id}, cliente_id={self.cliente_id}, mes='{self.mes}', estado='{self.estado}')"