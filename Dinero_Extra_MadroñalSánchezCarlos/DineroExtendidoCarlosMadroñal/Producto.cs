using System;
public class Producto
{
    public string Nombre { get; private set; }
    public double Precio { get; private set; }
    public string Categoria { get; private set; }

    public Producto(string nombre, double precio, string categoria)
    {
        Nombre = nombre;
        Precio = precio;
        Categoria = categoria;
    }

    public override string ToString()
    {
        return $"Producto: {Nombre}, Precio: {Precio} Euros, Categoría: {Categoria}";
    }
}