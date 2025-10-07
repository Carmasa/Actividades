using System;
using System.Text.RegularExpressions;

public class GastoExcepcion
{
    public static double VerificcacionSaldo()
    {
        while (true)
        {
            Console.Write("Ingresa el valor en euros: ");
            string saldoInput = Console.ReadLine();

            if (double.TryParse(saldoInput, out double saldo))
            {
                if (saldo < 0)
                {
                    Console.WriteLine("El valor no puede ser negativo.");
                }
                else
                {
                    Console.WriteLine($"valor válido: {saldo}");
                    return saldo; 
                }
            }
            else
            {
                Console.WriteLine("Por favor, ingresa un número válido.");
            }
        }
    }

    public static bool VerificacionDni(string dni)
    {
        if (string.IsNullOrEmpty(dni))
            return false;

        string patron = @"^\d{8}-?[A-Z]$";
        return Regex.IsMatch(dni, patron);
    }
}