# from odoo import http


# class Sesion(http.Controller):
#     @http.route('/sesion/sesion', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/sesion/sesion/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('sesion.listing', {
#             'root': '/sesion/sesion',
#             'objects': http.request.env['sesion.sesion'].search([]),
#         })

#     @http.route('/sesion/sesion/objects/<model("sesion.sesion"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('sesion.object', {
#             'object': obj
#         })

