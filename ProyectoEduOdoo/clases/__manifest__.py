{
    'name': "Clases",

    'summary': "Módulo para gestionar clases de las sesiones",

    'description': """
Módulo para la gestión de clases con información de horarios y grupos.
Relaciones con sesiones y profesores.
    """,

    'author': "Academia EduOdoo",
    'website': "https://www.yourcompany.com",

    'category': 'Education',
    'version': '0.1',

    'depends': ['base', 'profesor', 'sesion'],

    'data': [
        'security/ir.model.access.csv',
        'views/clases_views.xml',
    ],
    # only loaded in demonstration mode
    'demo': [
        'demo/demo.xml',
    ],
}

