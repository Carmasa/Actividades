from odoo import models, fields, api
from odoo.exceptions import ValidationError


class Matricula(models.Model):
    """
    Tabla intermedia que registra la inscripción de un alumno en un curso.
    Permite rastrear el estado de pago y la fecha de inscripción.
    """
    _name = 'matricula.matricula'
    _description = 'Matrícula de Alumno en Curso'

    # Campos relacionales
    alumno_id = fields.Many2one(
        'alumno.alumno',
        string='Alumno',
        required=True,
        ondelete='cascade',
        help='Alumno que se inscribe en el curso'
    )

    curso_id = fields.Many2one(
        'curso.curso',
        string='Curso',
        required=True,
        ondelete='cascade',
        help='Curso en el que se inscribe el alumno'
    )

    # Campos de estado
    estado = fields.Selection(
        selection=[
            ('borrador', 'Borrador'),
            ('confirmada', 'Confirmada'),
            ('pagada', 'Pagada'),
        ],
        string='Estado',
        default='borrador',
        required=True,
        help='Estado del flujo de pago de la matrícula'
    )

    # Campos informativos
    fecha_inscripcion = fields.Date(
        string='Fecha de Inscripción',
        default=fields.Date.today,
        help='Fecha en la que se realizó la inscripción'
    )

    # Constrains y validaciones
    _sql_constraints = []

    @api.constrains('alumno_id', 'curso_id')
    def _check_unique_matricula(self):
        """Validar que no exista una matrícula duplicada del mismo alumno en el mismo curso."""
        for record in self:
            duplicate = self.search([
                ('alumno_id', '=', record.alumno_id.id),
                ('curso_id', '=', record.curso_id.id),
                ('id', '!=', record.id),
            ])
            if duplicate:
                raise ValidationError(
                    f'El alumno {record.alumno_id.nombre} ya está inscrito en el curso {record.curso_id.titulo}'
                )

    def __str__(self):
        return f'{self.alumno_id.nombre} - {self.curso_id.titulo}'

    @api.depends('alumno_id', 'curso_id', 'estado')
    def _compute_display_name(self):
        """Personalizar el nombre que se muestra en vistas."""
        for record in self:
            record.display_name = f'{record.alumno_id.nombre} → {record.curso_id.titulo} ({record.estado})'
