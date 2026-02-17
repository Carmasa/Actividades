from odoo import models, fields, api


class Curso(models.Model):
    _name = 'curso.curso'
    _description = 'Curso'
    _rec_name = 'titulo'

    titulo = fields.Char(string="Título", required=True)
    descripcion = fields.Text(string="Descripción")
    nivel = fields.Selection([
        ('A1', 'A1 - Beginner'),
        ('A2', 'A2 - Elementary'),
        ('B1', 'B1 - Intermediate'),
        ('B2', 'B2 - Upper Intermediate'),
        ('C1', 'C1 - Advanced'),
        ('C2', 'C2 - Mastery')
    ], string="Nivel", required=True)
    precio = fields.Float(string="Precio", required=True)
    profesor_ids = fields.Many2many('profesor.profesor', string="Profesores")

