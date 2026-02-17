# from odoo import http


# class Curso(http.Controller):
#     @http.route('/curso/curso', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/curso/curso/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('curso.listing', {
#             'root': '/curso/curso',
#             'objects': http.request.env['curso.curso'].search([]),
#         })

#     @http.route('/curso/curso/objects/<model("curso.curso"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('curso.object', {
#             'object': obj
#         })

