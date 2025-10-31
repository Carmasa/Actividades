import java.util.Scanner;

public class Ejercicio2_ArbolesYGrafos {

    public static void main(String[] args) {
        Ejercicio2ListinTelefonico listin = new Ejercicio2ListinTelefonico();
        Scanner leer = new Scanner(System.in);
        int opcion=0;

        do {
            System.out.println("1) Alta");
            System.out.println("2) Baja");
            System.out.println("3) Modificación");
            System.out.println("4) Búsqueda por teléfono");
            System.out.println("5) Búsqueda por apellidos+nombre");
            System.out.println("6) Búsqueda por prefijo de apellidos");
            System.out.println("7) Búsqueda por rango alfabético");
            System.out.println("8) Mostrar todo");
            System.out.println("9) Salir");
            System.out.print("Elige una opción: ");

            if (!leer.hasNextInt()) {
                System.out.println("Opción inválida.");
                leer.next();
                continue;
            }
            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {
                case 1: {
                    System.out.print("Nombre: ");
                    String nombre = leer.nextLine();
                    System.out.print("Apellidos: ");
                    String apellido = leer.nextLine();
                    System.out.print("Teléfono: ");
                    String telf = leer.nextLine();
                    System.out.print("Email: ");
                    String email = leer.nextLine().trim();
                    if (email.isEmpty())
                        email = null;
                        listin.alta(new Ejercicio2Contacto(nombre, apellido, telf, email));
                    break;
                }
                case 2: {
                    System.out.print("Teléfono a eliminar: ");
                    String telf = leer.nextLine();
                    listin.bajaPorTelefono(telf);
                    break;
                }
                case 3: {
                    System.out.print("Teléfono actual: ");
                    String telf = leer.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombre = leer.nextLine();
                    System.out.print("Nuevos apellidos: ");
                    String apellido = leer.nextLine();
                    System.out.print("Nuevo teléfono: ");
                    String nuevoTelf = leer.nextLine();
                    System.out.print("Nuevo email (opcional): ");
                    String email = leer.nextLine().trim();
                    if (email.isEmpty())
                        email = null;
                        listin.modificar(telf, nombre, apellido, nuevoTelf, email);
                    break;
                }
                case 4: {
                    System.out.print("Teléfono: ");
                    String telf = leer.nextLine();
                    Ejercicio2Contacto contacto = listin.buscarPorTelefono(telf);
                    if (contacto != null)
                        System.out.println(contacto);
                    else
                        System.out.println("Contacto no encontrado.");
                    break;
                }
                case 5: {
                    System.out.print("Apellidos: ");
                    String apellido = leer.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = leer.nextLine();
                    Ejercicio2Contacto contacto = listin.buscarPorNombre(apellido, nombre);
                    if (contacto != null)
                        System.out.println("✅ " + contacto);
                    else
                        System.out.println("Contacto no encontrado.");
                    break;
                }
                case 6: {
                    System.out.print("Prefijo de apellidos: ");
                    String pref = leer.nextLine();
                    var busqueda = listin.buscarPorPrefijo(pref);
                    if (busqueda.isEmpty())
                        System.out.println("Ningún contacto coincide.");
                    else {
                        System.out.println("Resultados (" + busqueda.size() + "):");
                        busqueda.forEach(System.out::println);
                    }
                    break;
                }
                case 7: {
                    System.out.print("Apellido inicial: ");
                    String apeIni = leer.nextLine();
                    System.out.print("Apellido final: ");
                    String apeFin = leer.nextLine();
                    var rango = listin.buscarPorRango(apeIni, apeFin);
                    if (rango.isEmpty())
                        System.out.println("Ningun contacto en ese rango.");
                    else {
                        System.out.println("Resultados (" + rango.size() + "):");
                        rango.forEach(System.out::println);
                    }
                    break;
                }
                case 8:
                    listin.mostrarInorden();
                    break;
                case 9:
                    System.out.println("Fin del programa");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 9);

        leer.close();
    }
}