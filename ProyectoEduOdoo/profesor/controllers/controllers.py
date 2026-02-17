# from odoo import http


# class Profesor(http.Controller):
#     @http.route('/profesor/profesor', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/profesor/profesor/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('profesor.listing', {
#             'root': '/profesor/profesor',
#             'objects': http.request.env['profesor.profesor'].search([]),
#         })

#     @http.route('/profesor/profesor/objects/<model("profesor.profesor"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('profesor.object', {
#             'object': obj
#         })

