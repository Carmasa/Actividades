# from odoo import http


# class Alumno(http.Controller):
#     @http.route('/alumno/alumno', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/alumno/alumno/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('alumno.listing', {
#             'root': '/alumno/alumno',
#             'objects': http.request.env['alumno.alumno'].search([]),
#         })

#     @http.route('/alumno/alumno/objects/<model("alumno.alumno"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('alumno.object', {
#             'object': obj
#         })

