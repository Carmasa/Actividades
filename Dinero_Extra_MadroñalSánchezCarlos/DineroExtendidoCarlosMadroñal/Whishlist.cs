using System;
using System.Collections.Generic;

public class Wishlist
{
    private Cuenta Cuenta { get; set; }
    private List<Producto> Productos { get; set; }

    public Wishlist(Cuenta cuenta)
    {
        Cuenta = cuenta ?? throw new ArgumentNullException(nameof(cuenta));
        Productos = new List<Producto>();
    }
    /*
        public void AddProducto(Producto producto)
        {
            if (producto != null)
                Productos.Add(producto);
        }
    */
    public void AddProducto(string nombre, double precio, string categoria)
    {
        Productos.Add(new Producto(nombre, precio, categoria));
    }
    public List<Producto> GetProductos()
    {
        return new List<Producto>(Productos);
    }

    public List<Producto> GetProductosParaComprar()
    {
        List<Producto> comprables = new List<Producto>();
        double gastosImprescindibles = Cuenta.GetGastosImprescindibles();
        double saldoDisponible = Cuenta.Saldo;

        foreach (var producto in Productos)
        {
            // ¿Puedo comprar este producto y aún cubrir gastos imprescindibles?
            if (saldoDisponible >= producto.Precio + gastosImprescindibles)
            {
                comprables.Add(producto);
            }
        }

        return comprables;
    }
}