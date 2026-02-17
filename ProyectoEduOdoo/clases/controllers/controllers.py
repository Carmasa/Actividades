# from odoo import http


# class Clases(http.Controller):
#     @http.route('/clases/clases', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/clases/clases/objects', auth='public')
#     def list(self, **kw):
#         return http.request.render('clases.listing', {
#             'root': '/clases/clases',
#             'objects': http.request.env['clases.clases'].search([]),
#         })

#     @http.route('/clases/clases/objects/<model("clases.clases"):obj>', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('clases.object', {
#             'object': obj
#         })

