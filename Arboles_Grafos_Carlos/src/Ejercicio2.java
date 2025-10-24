import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Ejercicio2 {

    public static void main(String[] args) {
        Queue<String> colaClientes = new LinkedList<>();
        Scanner leer = new Scanner(System.in);
        int opcion=0;

        do {
            System.out.println("\nMenú:");
            System.out.println("1) Llegada");
            System.out.println("2) Atender");
            System.out.println("3) Mostrar cola");
            System.out.println("4) Salir");
            System.out.print("Elige una opción: ");

            if (!leer.hasNextInt()) {
                System.out.println("Error. Introduce un número.");
                leer.next();
                continue;
            }
            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Introduce el nombre del cliente: ");
                    String nombre = leer.nextLine().trim();
                    if (nombre.isEmpty()) {
                        System.out.println("No se añadio ningun nombre.");
                    } else {
                        colaClientes.offer(nombre);
                        System.out.println(nombre + "' añadido a la cola.");
                    }
                    break;

                case 2:
                    if (colaClientes.isEmpty()) {
                        System.out.println("No hay clientes en la cola.");
                    } else {
                        String atendido = colaClientes.poll();
                        System.out.println("Atendiendo a: " + atendido);
                    }
                    break;

                case 3:
                    if (colaClientes.isEmpty()) {
                        System.out.println("La cola está vacía.");
                    } else {
                        System.out.println("Cola actual:");
                        System.out.println(colaClientes);
                    }
                    break;

                case 4:
                    System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                    break;

                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }

        } while (opcion != 4);

        leer.close();
    }
}