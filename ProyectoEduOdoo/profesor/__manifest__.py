{
    'name': "Profesor",

    'summary': "Módulo para gestionar profesores de los cursos",

    'description': """
Módulo para la gestión de profesores con información de nombre, apellidos y titulación.
Relaciones con cursos y clases.
    """,

    'author': "Academia EduOdoo",
    'website': "https://www.yourcompany.com",

    'category': 'Education',
    'version': '0.1',

    'depends': ['base'],

    # always loaded
    'data': [
        'security/ir.model.access.csv',
        'views/profesor_views.xml',
    ],
    # only loaded in demonstration mode
    'demo': [
        'demo/demo.xml',
    ],
}

