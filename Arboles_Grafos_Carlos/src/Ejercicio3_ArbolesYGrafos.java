import java.util.*;

public class Ejercicio3_ArbolesYGrafos {
    public static void main(String[] args) {
        Ejercicio3GrafoRutas grafo = new Ejercicio3GrafoRutas();
        grafo.cargarGrafoEjemplo();

        Scanner leer = new Scanner(System.in);
        System.out.print("Para el ejercicio se han creado diferentes opcciones de rutas por defecto para origen y destino ");
        System.out.println("\nZonas: A (Casa), B (Estación Metro), C (Centro), D (Trabajo), E (Ilerna)");

        System.out.print("Origen: ");
        String origen = leer.nextLine().trim();
        System.out.print("Destino: ");
        String destino = leer.nextLine().trim();

        Set<Ejercicio3ModoTransporte> prohibidos = new HashSet<>();
        System.out.println("\n¿Quieres evitar algún modo de transporte?");
        System.out.println("1) Bicicleta");
        System.out.println("2) Autobús");
        System.out.println("3) Metro");
        System.out.println("4) Ninguno");
        System.out.print("Elige (puedes escribir varios separados por comas): ");
        String opcion = leer.nextLine().trim();
        if (!opcion.equals("4") && !opcion.isEmpty()) {
            String[] opciones = opcion.split(",");
            for (String op : opciones) {
                switch (op.trim()) {
                    case "1":
                        prohibidos.add(Ejercicio3ModoTransporte.BICI)
                        ; break;
                    case "2":
                        prohibidos.add(Ejercicio3ModoTransporte.AUTOBUS)
                        ; break;
                    case "3":
                        prohibidos.add(Ejercicio3ModoTransporte.METRO)
                        ; break;
                }
            }
        }

        System.out.print("\n¿Minimizar transbordos? (s/n): ");
        boolean minimizar = leer.nextLine().trim().equalsIgnoreCase("s");

        Ejercicio3Ruta ruta = grafo.buscarRuta(origen, destino, prohibidos, minimizar);
        ruta.imprimir();

        leer.close();
    }
}