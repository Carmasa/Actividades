using System;
using System.Collections.Generic;

public class Program
{
    public static void Main(string[] args)
    {
        string dni = "";
        Console.WriteLine("Dime Nombre");
        string nombre = Console.ReadLine();

        int edad = 0;
        while (true)
        {
            Console.Write("Ingresa tu edad: ");
            string edadInput = Console.ReadLine();

            if (int.TryParse(edadInput, out edad))
            {
                if (edad >= 18 && edad <= 120)
                {
                    Console.WriteLine($"Edad válida: {edad}");
                    break;
                }
                else
                {
                    Console.WriteLine("La edad debe estar entre 18 y 120.");
                }
            }
            else
            {
                Console.WriteLine("Por favor, ingresa un número válido.");
            }
        }

        while (!GastoExcepcion.VerificacionDni(dni))
        {
            Console.WriteLine("Inserte DNI");
            dni = Console.ReadLine();
            if (!GastoExcepcion.VerificacionDni(dni))
            {
                Console.WriteLine("DNI invalido");
            }
            else
            {
                Console.WriteLine("DNI validadO correctamente");
            }
        }

        Usuario usuario = new Usuario(nombre, edad, dni);
        Cuenta cuenta = new Cuenta(usuario);
        Wishlist wishlist = new Wishlist(cuenta);


        bool salir = false;

        while (!salir)
        {
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine($"\nElige una opción, {nombre}:");
            Console.WriteLine("1 Introduce un nuevo gasto básico");
            Console.WriteLine("2 Introduce un nuevo gasto extra");
            Console.WriteLine("3 Introduce un nuevo ingreso");
            Console.WriteLine("4 Mostrar gastos");
            Console.WriteLine("5 Mostrar ingresos");
            Console.WriteLine("6 Mostrar saldo");
            Console.WriteLine("7 Mostrar ahorro de un período");
            Console.WriteLine("8 Mostrar gastos imprescindibles");
            Console.WriteLine("9 Mostrar posibles ahorros del mes pasado");
            Console.WriteLine("10 Mostrar lista de deseos");
            Console.WriteLine("11 Mostrar productos que podemos comprar");
            Console.WriteLine("12 Añadir producto a la Whislist");
            Console.WriteLine("0 Salir");
            Console.ForegroundColor = ConsoleColor.White;
            string opcion = Console.ReadLine();


            switch (opcion)
            {
                case "1":
                    Console.WriteLine("Introduce un nuevo gasto básico");
                    double dineroBasico = GastoExcepcion.VerificcacionSaldo();
                    Console.WriteLine("Introduce descripción");
                    string descBasico = Console.ReadLine();

                    cuenta.GastarDineroBasico(dineroBasico, descBasico);

                    break;

                case "2":
                    Console.WriteLine("Introduce un nuevo gasto extra");
                    double dineroExtra = GastoExcepcion.VerificcacionSaldo();
                    Console.WriteLine("Introduce descripción");
                    string descExtra = Console.ReadLine();
                    Console.Write("¿Es prescindible? (s/n): ");
                    string presc = Console.ReadLine().ToLower();
                    bool prescindible = presc == "s" || presc == "si" || presc == "y" || presc == "yes";
                    cuenta.GastarDineroExtra(dineroExtra, descExtra, prescindible);

                    break;

                case "3":
                    Console.WriteLine("Introduce un nuevo ingreso");
                    double dineroIngreso = GastoExcepcion.VerificcacionSaldo();
                    Console.WriteLine("Introduce descripción");
                    string descIngreso = Console.ReadLine();
                    cuenta.IngresarDinero(dineroIngreso, descIngreso);
                    break;

                case "4":
                    Console.WriteLine("¿Qué gastos quieres ver?");
                    Console.WriteLine("1. Todos");
                    Console.WriteLine("2. Básicos");
                    Console.WriteLine("3. Extras");
                    string tipoGasto = Console.ReadLine();
                    if (tipoGasto == "1")
                    {
                        cuenta.MostrarGastos();
                    }
                    else if (tipoGasto == "2")
                    {
                        var basicos = cuenta.GetGastosBasicos(esteMes: false);
                        if (basicos.Count == 0)
                            Console.WriteLine("No hay gastos básicos.");
                        else
                            basicos.ForEach(g => Console.WriteLine(g));
                    }
                    else if (tipoGasto == "3")
                    {
                        var extras = cuenta.GetGastosExtras(esteMes: false);
                        if (extras.Count == 0)
                            Console.WriteLine("No hay gastos extras.");
                        else
                            extras.ForEach(g => Console.WriteLine(g));
                    }
                    else
                    {
                        Console.WriteLine("Opción no válida.");
                    }
                    break;

                case "5":
                    cuenta.MostrarIngresos();
                    break;

                case "6":
                    Console.WriteLine($"Tu saldo es de: {cuenta.Saldo} Euros.");
                    break;

                case "7":
                    double ahorro = cuenta.GetAhorro();
                    Console.WriteLine($"Ahorro del mes actual: {ahorro:F2} Euros.");
                    break;

                case "8":
                    double gastosImp = cuenta.GetGastosImprescindibles();
                    Console.WriteLine($"Gastos imprescindibles del mes actual: {gastosImp:F2} Euros.");
                    break;

                case "9":
                    double ahorroMesPasado = cuenta.GetAhorroMesPasado();
                    Console.WriteLine($"Posibles ahorros del mes pasado: {ahorroMesPasado:F2} Euros.");
                    break;

                case "10":
                    var productos = wishlist.GetProductos();
                    if (productos.Count == 0)
                        Console.WriteLine("Tu lista de deseos está vacía.");
                    else
                    {
                        Console.WriteLine("Lista de deseos:");
                        productos.ForEach(p => Console.WriteLine(p));
                    }
                    break;

                case "11":
                    var comprables = wishlist.GetProductosParaComprar();
                    if (comprables.Count == 0)
                        Console.WriteLine("No puedes comprar ningún producto de tu lista de deseos por ahora.");
                    else
                    {
                        Console.WriteLine("Productos que puedes comprar:");
                        comprables.ForEach(p => Console.WriteLine(p));
                    }
                    break;
                case "12":
                    Console.WriteLine("Añadir producto a la wishlist");
                    Console.Write("Nombre del producto: ");
                    string nombreProd = Console.ReadLine();

                    Console.Write("Precio: ");
                    double precioProd = GastoExcepcion.VerificcacionSaldo();

                    Console.Write("Categoría: ");
                    string categoriaProd = Console.ReadLine();

                    wishlist.AddProducto(nombreProd, precioProd, categoriaProd);
                    Console.WriteLine("Producto añadido a la lista de deseos.");
                    break;

                case "0":
                    Console.ForegroundColor = ConsoleColor.Yellow;
                    Console.WriteLine("Fin del programa.");
                    Console.WriteLine("Gracias por utilizar la aplicación");
                    Console.ResetColor();
                    Console.WriteLine("¡Buen trabajo!");
                    salir = true;
                    break;
                default:
                    Console.WriteLine("Opción inválida. Por favor, elige una opción del 0 al 12.");
                    break;
            }

        }
    }
}