using ProyectoWPF.Data;
using ProyectoWPF.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProyectoWPF.Services
{
    public class CarnesService
    {

        // READ: Obtener datos (Usa LINQ)
        public List<Carnes> ObtenerCarnes()
        {
            using (var db = new AppDbContext())

            {

                 return db.Carnes.OrderBy(c => c.Nombre).ToList();
            }
        }

    }
}

//  return db.Carnes.OrderBy(d => d.Descripcion).Select<Carnes>.ToList();

/*
 var resultado = 
db.Clientes .OrderBy(c => c.Nombre) .Select(
c => new { // 1. Campos directamente de la tabla (Cliente) ID = c.IdCliente, NombreCompleto = c.Nombre, Ciudad = c.Direccion.Ciudad}).ToList();  


//  return db.Carnes.OrderBy(c => c.Nombre).ToList();


//  return db.Carnes.OrderBy(d => d.Descripcion).Select<Carnes>.ToList();


d => d.Descripcion, p => p.precio
*/

