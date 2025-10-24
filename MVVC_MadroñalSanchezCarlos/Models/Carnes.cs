using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace ProyectoWPF.Models
{
    public class Carnes
    {
        public int Id { get; set; }
        public string? Nombre { get; set; } // Campo principal a guardar
        public string? Descripcion { get; set; }
        public Decimal? Precio { get; set; }
        public string? Disponibilidad { get; set; }

    }
}
