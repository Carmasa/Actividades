public class Ingreso : Dinero
{
    public DateTime Fecha { get; private set; }

    public Ingreso(double cantidad, string descripcion, DateTime fecha)
        : base(cantidad, descripcion)
    {
        Fecha = fecha;
    }

    public override string ToString()
    {
        return $"Ingreso: {Cantidad}, Descripción: {Descripcion}, Fecha: {Fecha:yyyy-MM-dd} Euros.\n";
    }
}