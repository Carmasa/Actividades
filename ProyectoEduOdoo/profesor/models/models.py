from odoo import models, fields, api


class Profesor(models.Model):
    _name = 'profesor.profesor'
    _description = 'Profesor'

    nombre = fields.Char(string="Nombre", required=True)
    apellidos = fields.Char(string="Apellidos", required=True)
    titulacion = fields.Char(string="Titulación", required=True)
    email = fields.Char(string="Email")

    @api.depends('nombre', 'apellidos')
    def _compute_display_name(self):
        for record in self:
            record.display_name = f"{record.nombre} {record.apellidos}"

