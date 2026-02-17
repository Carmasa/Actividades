{
    'name': 'Matrícula',
    'version': '1.0',
    'category': 'Education',
    'summary': 'Módulo de gestión de matrículas de alumnos en cursos',
    'author': 'EduOdoo Team',
    'website': 'https://eduodoo.com',
    'depends': ['base', 'profesor', 'alumno', 'curso'],
    'data': [
        'security/ir.model.access.csv',
        'views/matricula_views.xml',
    ],
    'installable': True,
    'application': True,
    'license': 'LGPL-3',
}
