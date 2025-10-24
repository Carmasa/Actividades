using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace ProyectoWPF.Models
{
    public class Especialidades
    {
        public int Id { get; set; }
        public string? Nombre { get; set; } // Campo principal a guardar
        public string? Descripcion { get; set; }

        public Double? Precio { get; set; }
        public string? Disponibilidad { get; set; }

        public Productos Productos
        {
            get => default;
            set
            {
            }
        }
    }
}
