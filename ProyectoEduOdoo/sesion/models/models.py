from odoo import models, fields, api
from odoo.exceptions import ValidationError
from datetime import datetime, timedelta


class Sesion(models.Model):
    _name = 'sesion.sesion'
    _description = 'Sesión'
    _rec_name = 'titulo'

    titulo = fields.Char(string="Título", required=True)
    fecha_inicio = fields.Datetime(string="Fecha de Inicio", required=True)
    duracion = fields.Integer(string="Duración (minutos)", required=True)
    numero_asientos = fields.Integer(string="Número de Asientos", required=True)
    curso_id = fields.Many2one('curso.curso', string="Curso", required=True)
    profesor_id = fields.Many2one('profesor.profesor', string="Profesor", help="Profesor que imparte la sesión")
    fecha_fin = fields.Datetime(string="Fecha de Fin", compute='_compute_fecha_fin', store=True)

    @api.depends('fecha_inicio', 'duracion')
    def _compute_fecha_fin(self):
        for record in self:
            if record.fecha_inicio and record.duracion:
                record.fecha_fin = record.fecha_inicio + timedelta(minutes=record.duracion)
            else:
                record.fecha_fin = record.fecha_inicio
    
    estado = fields.Selection(
        selection=[
            ('borrador', 'Borrador'),
            ('confirmada', 'Confirmada'),
            ('pagada', 'Pagada'),
        ],
        string='Estado',
        default='borrador',
        required=True,
        help='Estado de la sesión en el flujo de pago'
    )
    
    # Campos computed para ocupación
    alumnos_inscritos = fields.Integer(
        compute='_compute_alumnos_inscritos',
        string='Alumnos Inscritos',
        help='Número de alumnos inscritos en el curso de esta sesión'
    )
    
    porcentaje_ocupacion = fields.Float(
        compute='_compute_porcentaje_ocupacion',
        string='Porcentaje de Ocupación (%)',
        help='Porcentaje de asientos ocupados'
    )
    
    color_sesion = fields.Integer(
        compute='_compute_color_sesion',
        string='Color de Sesión',
        help='Color para ProgressBar: 0=normal, 10=llena'
    )
    
    @api.depends('curso_id')
    def _compute_alumnos_inscritos(self):
        """Calcular el número de alumnos inscritos en el curso."""
        for record in self:
            matriculas = self.env['matricula.matricula'].search([
                ('curso_id', '=', record.curso_id.id),
                ('estado', '!=', 'borrador')
            ])
            record.alumnos_inscritos = len(matriculas)
    
    @api.depends('alumnos_inscritos', 'numero_asientos')
    def _compute_porcentaje_ocupacion(self):
        """Calcular el porcentaje de ocupación."""
        for record in self:
            if record.numero_asientos > 0:
                record.porcentaje_ocupacion = (record.alumnos_inscritos / record.numero_asientos) * 100
            else:
                record.porcentaje_ocupacion = 0.0
    
    @api.depends('porcentaje_ocupacion')
    def _compute_color_sesion(self):
        """Cambiar color si la sesión está llena (ProgressBar)."""
        for record in self:
            if record.porcentaje_ocupacion >= 100:
                record.color_sesion = 10  # Rojo: sesión llena
            elif record.porcentaje_ocupacion >= 80:
                record.color_sesion = 9   # Naranja: casi llena
            elif record.porcentaje_ocupacion >= 50:
                record.color_sesion = 5   # Amarillo: medio llena
            else:
                record.color_sesion = 0   # Verde: con espacios
    
    @api.constrains('alumnos_inscritos', 'numero_asientos')
    def _check_capacidad_sesion(self):
        """Validar que no se supere la capacidad de la sesión."""
        for record in self:
            if record.alumnos_inscritos > record.numero_asientos:
                raise ValidationError(
                    f'La sesión {record.titulo} tiene más alumnos ({record.alumnos_inscritos}) '
                    f'que asientos disponibles ({record.numero_asientos})'
                )
    
    @api.constrains('profesor_id', 'fecha_inicio', 'duracion')
    def _check_profesor_conflicto_horario(self):
        """Validar que un profesor no tenga dos sesiones en el mismo horario."""
        for record in self:
            if not record.profesor_id:
                continue
            
            # Calcular la hora de fin de esta sesión
            fecha_fin = record.fecha_inicio + timedelta(minutes=record.duracion)
            
            # Buscar otras sesiones del mismo profesor que se solapen
            conflictos = self.search([
                ('profesor_id', '=', record.profesor_id.id),
                ('id', '!=', record.id),
                ('fecha_inicio', '<', fecha_fin),
                ('fecha_inicio', '>', record.fecha_inicio - timedelta(minutes=1)),
            ])
            
            if conflictos:
                profesor_nombre = record.profesor_id.nombre
                raise ValidationError(
                    f'El profesor {profesor_nombre} ya tiene otra sesión programada '
                    f'en el rango de horario {record.fecha_inicio} - {fecha_fin}'
                )

