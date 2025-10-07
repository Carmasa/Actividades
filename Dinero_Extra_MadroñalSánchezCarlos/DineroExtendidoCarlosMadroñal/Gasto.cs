using System;

public class Gasto : Dinero
{
    public Gasto(double cantidad, string descripcion) : base(cantidad, descripcion)
    {
    }

    public override string ToString()
    {
        return $"Gasto: descripción = '{Descripcion}', dinero = {Cantidad} Euros.\n";
    }
}
