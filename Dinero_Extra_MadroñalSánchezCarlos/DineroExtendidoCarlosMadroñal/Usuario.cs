using System;
using System.Text.RegularExpressions;

public class Usuario
{
    public string Nombre { get; private set; }
    public int Edad { get; private set; }
    public string Dni { get; private set; }

    public Usuario(string nombre, int edad, string dni)
    {
        Nombre = nombre;
        Edad = edad;
        Dni = dni; 
    }


    public override string ToString()
    {
        return $"{Nombre} {Edad} {Dni}";
    }
}