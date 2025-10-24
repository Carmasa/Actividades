using ProyectoWPF.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.SqlServer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace ProyectoWPF.Data
{
    public class AppDbContext : DbContext
    {
        public DbSet<Carnes> Carnes { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            // Usa tu cadena de conexión a SQL Server
            optionsBuilder.UseSqlServer("Server=DESKTOP-SV84OU8\\SQLEXPRESS;Database=Restaurante;Integrated Security=True;TrustServerCertificate=True");
        }
    }
}
