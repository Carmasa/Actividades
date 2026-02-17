from odoo import models, fields, api


class Clases(models.Model):
    _name = 'clases.clases'
    _description = 'Clase'
    _rec_name = 'nombre'

    nombre = fields.Char(string="Nombre de la Clase", required=True)
    horario = fields.Char(string="Horario", required=True)
    grupo = fields.Char(string="Grupo", required=True)
    sesion_id = fields.Many2one('sesion.sesion', string="Sesión", required=True)
    profesor_id = fields.Many2one('profesor.profesor', string="Profesor", required=True)

