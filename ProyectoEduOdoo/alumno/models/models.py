from odoo import models, fields, api


class Alumno(models.Model):
    _name = 'alumno.alumno'
    _description = 'Alumno'

    nombre = fields.Char(string="Nombre", required=True)
    apellidos = fields.Char(string="Apellidos", required=True)
    email = fields.Char(string="Email", required=True)
    
    @api.depends('nombre', 'apellidos')
    def _compute_display_name(self):
        for record in self:
            record.display_name = f"{record.nombre} {record.apellidos}"
    
    # Campo computed que obtiene los cursos a través de búsqueda en matriculas
    curso_ids = fields.Many2many(
        'curso.curso',
        compute='_compute_cursos',
        string="Cursos",
        help="Cursos en los que está inscrito"
    )
    
    @api.depends()
    def _compute_cursos(self):
        """Calcular los cursos a través de búsqueda en matriculas."""
        for record in self:
            # Búsqueda dinámica sin dependencia en One2many
            matriculas = self.env['matricula.matricula'].search([
                ('alumno_id', '=', record.id)
            ])
            record.curso_ids = matriculas.mapped('curso_id')
    
    _sql_constraints = [
        ('email_unico', 'UNIQUE(email)', 'El email debe ser único')
    ]

