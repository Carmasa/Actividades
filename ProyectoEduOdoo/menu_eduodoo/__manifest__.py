{
    'name': 'Menú EduOdoo',
    'version': '1.0',
    'category': 'Education',
    'summary': 'Menú centralizado para la gestión de EduOdoo',
    'author': 'EduOdoo Team',
    'website': 'https://eduodoo.com',
    'depends': ['base', 'profesor', 'curso', 'alumno', 'sesion', 'clases', 'matricula', 'facturacion'],
    'data': [
        'views/menu.xml',
    ],
    'installable': True,
    'application': True,
    'license': 'LGPL-3',
}
