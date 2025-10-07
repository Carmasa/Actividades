using System;

public abstract class Dinero
{
    public double Cantidad { get; protected set; }
    public string Descripcion { get; protected set; }

    public Dinero(double cantidad, string descripcion)
    {
        Cantidad = cantidad;
        Descripcion = descripcion;
    }

    public override string ToString()
    {
        return $"Dinero: {Cantidad}, Descripción: {Descripcion} Euros.\n";
    }
}