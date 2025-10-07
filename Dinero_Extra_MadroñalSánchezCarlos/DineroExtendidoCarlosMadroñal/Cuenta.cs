using System;
using System.Collections.Generic;

public class Cuenta
{
    public double Saldo { get; private set; }
    public Usuario Usuario { get; private set; }
    private List<Gasto> ListaGastos { get; set; }
    private List<Ingreso> ListaIngresos { get; set; }

    public Cuenta(Usuario usuario)
    {
        Usuario = usuario;
        Saldo = 0;
        ListaGastos = new List<Gasto>();
        ListaIngresos = new List<Ingreso>();
    }

    public double IngresarDinero(double dinero, string descripcion)
    {
        DateTime fechaActual = DateTime.Now;
        Ingreso ingreso = new Ingreso(dinero, descripcion, fechaActual);
        Saldo = Saldo + ingreso.Cantidad;
        ListaIngresos.Add(ingreso);
        Console.WriteLine($"Ingreso realizado correctamente de: {ingreso.Cantidad} Euros.");
        return Saldo;
    }

    public double GastarDineroBasico(double dinero, string descripcion)
    {
        if (dinero > Saldo)
        {
            Console.WriteLine("No se puede realizar el gasto");
            return Saldo;
        }

        DateTime fechaActual = DateTime.Now;
        GastoBasico gasto = new GastoBasico(dinero, descripcion, fechaActual);
        Saldo = Saldo - gasto.Cantidad;
        ListaGastos.Add(gasto);
        Console.WriteLine($"Gasto realizado correctamente de: {gasto.Cantidad} Euros.");
        return Saldo;
    }

    public double GastarDineroExtra(double dinero, string descripcion, bool prescindible)
    {
        if (dinero > Saldo)
        {
            Console.WriteLine("No se puede realizar el gasto");
            return Saldo;
        }

        DateTime fechaActual = DateTime.Now;
        GastoExtra gasto = new GastoExtra(dinero, descripcion, fechaActual, prescindible);
        Saldo = Saldo - gasto.Cantidad;
        ListaGastos.Add(gasto);
        Console.WriteLine($"Gasto realizado correctamente de: {gasto.Cantidad} Euros.");
        return Saldo;
    }

    public List<GastoBasico> GetGastosBasicos(bool esteMes)
    {
        List<GastoBasico> resultado = new List<GastoBasico>();
        DateTime hoy = DateTime.Now;

        foreach (var gasto in ListaGastos)
        {
            if (gasto is GastoBasico basico)
            {
                if (!esteMes || (basico.Fecha.Year == hoy.Year && basico.Fecha.Month == hoy.Month))
                {
                    resultado.Add(basico);
                }
            }
        }

        return resultado;
    }

    public List<GastoExtra> GetGastosExtras(bool esteMes)
    {
        List<GastoExtra> resultado = new List<GastoExtra>();
        DateTime hoy = DateTime.Now;

        foreach (var gasto in ListaGastos)
        {
            if (gasto is GastoExtra extra)
            {
                if (!esteMes || (extra.Fecha.Year == hoy.Year && extra.Fecha.Month == hoy.Month))
                {
                    resultado.Add(extra);
                }
            }
        }

        return resultado;
    }
    public double GetAhorro()
    {
        DateTime hoy = DateTime.Now;
        double ingresosMes = 0;
        double gastosMes = 0;

        foreach (var ingreso in ListaIngresos)
        {
            if (ingreso.Fecha.Year == hoy.Year && ingreso.Fecha.Month == hoy.Month)
            {
                ingresosMes += ingreso.Cantidad;
            }
        }

        foreach (var gasto in ListaGastos)
        {
            if (gasto is GastoBasico basico)
            {
                if (basico.Fecha.Year == hoy.Year && basico.Fecha.Month == hoy.Month)
                    gastosMes = gastosMes+ basico.Cantidad;
            }
            else if (gasto is GastoExtra extra)
            {
                if (extra.Fecha.Year == hoy.Year && extra.Fecha.Month == hoy.Month)
                    gastosMes = gastosMes + extra.Cantidad;
            }
        }

        return ingresosMes - gastosMes;
    }

    public double GetGastosImprescindibles()
    {
        DateTime hoy = DateTime.Now;
        double total = 0;

        foreach (var gasto in ListaGastos)
        {
            if (gasto is GastoBasico basico)
            {
                if (basico.Fecha.Year == hoy.Year && basico.Fecha.Month == hoy.Month)
                    total = total + basico.Cantidad;
            }
            else if (gasto is GastoExtra extra)
            {
                if (!extra.Prescindible &&
                    extra.Fecha.Year == hoy.Year && extra.Fecha.Month == hoy.Month)
                {
                    total = total+ extra.Cantidad;
                }
            }
        }

        return total;
    }



    public void MostrarGastos()
    {
        if (ListaGastos.Count == 0)
        {
            Console.WriteLine("No hay gastos registrados.");
            return;
        }

        foreach (var gasto in ListaGastos)
        {
            Console.WriteLine(gasto);
        }
    }

    public void MostrarIngresos()
    {
        if (ListaIngresos.Count == 0)
        {
            Console.WriteLine("No hay ingresos registrados.");
            return;
        }

        foreach (var ingreso in ListaIngresos)
        {
            Console.WriteLine(ingreso);
        }
    }
    public double GetAhorroMesPasado()
    {
        DateTime hoy = DateTime.Now;
        int mesPasado = hoy.Month == 1 ? 12 : hoy.Month - 1;
        int añoPasado = hoy.Month == 1 ? hoy.Year - 1 : hoy.Year;

        double totalPrescindibles = 0;

        // Recorremos todos los gastos extras (no solo del mes actual)
        foreach (var gasto in ListaGastos)
        {
            if (gasto is GastoExtra extra)
            {
                // Verificamos si es del mes pasado y es prescindible
                if (extra.Prescindible &&
                    extra.Fecha.Year == añoPasado &&
                    extra.Fecha.Month == mesPasado)
                {
                    totalPrescindibles += extra.Cantidad;
                }
            }
        }

        return totalPrescindibles;
    }

    public override string ToString()
    {
        return $"{Usuario}\nSaldo actual: {Saldo} Euros.\n";
    }
}