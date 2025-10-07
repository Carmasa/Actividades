using System;
public class GastoExtra : Gasto
{
    public DateTime Fecha { get; private set; }
    public bool Prescindible { get; private set; }

    public GastoExtra(double cantidad, string descripcion, DateTime fecha, bool prescindible)
        : base(cantidad, descripcion)
    {
        Fecha = fecha;
        Prescindible = prescindible;
    }

    public override string ToString()
    {
        return $"Gasto Extra: {Cantidad}, Descripción: {Descripcion}, Fecha: {Fecha:yyyy-MM-dd}, Prescindible: {Prescindible} Euros.\n";
    }
}
