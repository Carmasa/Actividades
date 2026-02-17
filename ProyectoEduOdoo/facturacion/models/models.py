from odoo import models, fields, api


class Facturacion(models.Model):
    _name = 'facturacion.facturacion'
    _description = 'Facturación'

    numero_factura = fields.Char(string="Número de Factura", required=True)
    matricula_id = fields.Many2one('matricula.matricula', string="Matrícula", required=True, ondelete='cascade')
    
    # Campos relacionados para referencia rápida
    alumno_id = fields.Many2one(related='matricula_id.alumno_id', string="Alumno", readonly=True)
    curso_id = fields.Many2one(related='matricula_id.curso_id', string="Curso", readonly=True)
    
    cantidad = fields.Float(string="Cantidad", required=True)
    fecha_pago = fields.Date(string="Fecha de Pago")
    concepto = fields.Text(string="Concepto", required=True)
    estado = fields.Selection([
        ('draft', 'Borrador'),
        ('pendiente', 'Pendiente'),
        ('pagada', 'Pagada'),
        ('anulada', 'Anulada')
    ], string="Estado", default='draft')

