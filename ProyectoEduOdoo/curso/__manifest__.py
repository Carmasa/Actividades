{
    'name': "Curso",

    'summary': "Módulo para gestionar cursos de la academia",

    'description': """
Módulo para la gestión de cursos con información de título, descripción, nivel y precio.
Relaciones con profesores, sesiones y alumnos.
    """,

    'author': "Academia EduOdoo",
    'website': "https://www.yourcompany.com",

    # Categories can be used to filter modules in modules listing
    # Check https://github.com/odoo/odoo/blob/15.0/odoo/addons/base/data/ir_module_category_data.xml
    # for the full list
    'category': 'Education',
    'version': '0.1',

    # any module necessary for this one to work correctly
    'depends': ['base', 'profesor'],

    'data': [
        'security/ir.model.access.csv',
        'views/curso_views.xml',
    ],
    # only loaded in demonstration mode
    'demo': [
        'demo/demo.xml',
    ],
}

