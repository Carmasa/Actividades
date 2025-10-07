using System;

public class GastoBasico : Gasto
{
    public DateTime Fecha { get; private set; }

    public GastoBasico(double cantidad, string descripcion, DateTime fecha)
        : base(cantidad, descripcion)
    {
        Fecha = fecha;
    }

    public override string ToString()
    {
        return $"Gasto Básico: {Cantidad}, Descripción: {Descripcion}, Fecha: {Fecha:yyyy-MM-dd} Euros.\n";
    }
}