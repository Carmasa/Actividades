package com.mycompany.fruteriajaxb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LecturaJAXB {

    public static void main(String[] args) throws JAXBException, IOException {
        Scanner leer = new Scanner(System.in);

        // 1️⃣ PEDIR DATOS DEL CLIENTE
        System.out.println("=== BIENVENIDO A LA FRUTERÍA ===");
        System.out.print("Introduce tu nombre: ");
        String nombreCliente = leer.nextLine();

        System.out.print("Introduce tu email: ");
        String email = leer.nextLine();

        System.out.print("Introduce tu calle: ");
        String calle = leer.nextLine();

        System.out.print("Número: ");
        String numero = leer.nextLine();

        System.out.print("Ciudad: ");
        String ciudad = leer.nextLine();

        System.out.print("País: ");
        String pais = leer.nextLine();

        // 2️⃣ CARGAR FRUTERÍA DESDE XML
        JAXBContext context = JAXBContext.newInstance(Fruteria.class, Fruta.class, Verdura.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Fruteria fruteria = (Fruteria) unmarshaller.unmarshal(new File("Fruteria.xml"));

        GestionFruteria gestion = new GestionFruteria();

        // 3️⃣ PREPARAR ESTRUCTURAS JSON
        JSONArray arrayPedidos = new JSONArray();

        int opcion = -1;
        do {
            try {
                System.out.println("\n---- MENÚ FRUTERÍA ----");
                System.out.println("1. Mostrar catálogo completo");
                System.out.println("2. Mostrar frutas por temporada");
                System.out.println("3. Mostrar verduras por tipo");
                System.out.println("4. Mostrar ofertas por estación");
                System.out.println("5. Realizar pedido");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");

                opcion = Integer.parseInt(leer.nextLine());

                switch (opcion) {
                    case 1:
                        gestion.mostrarCatalogo(fruteria);
                        break;
                    case 2:
                        gestion.mostrarFrutasTemporada(fruteria);
                        break;
                    case 3:
                        gestion.mostrarVerdurasPorTipo(fruteria);
                        break;
                    case 4:
                        System.out.print("Introduce la estación (Otoño, Invierno, Primavera, Verano): ");
                        String estacion = leer.nextLine();
                        gestion.mostrarOfertasPorEstacion(fruteria, estacion);
                        break;
                    case 5:
                        JSONObject pedido = realizarPedido(fruteria, leer);
                        if (pedido != null) {
                            arrayPedidos.add(pedido);
                            System.out.println("Pedido añadido correctamente.");
                        }
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("️Opción no válida. Intenta de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número de opción válido.");
            }
        } while (opcion != 0);

        if (!arrayPedidos.isEmpty()) {
            guardarJSON(fruteria, nombreCliente, email, calle, numero, ciudad, pais, arrayPedidos);
            System.out.println("\nArchivo 'fruteria.json' generado con éxito con tu pedido.");
        } else {
            System.out.println("\nℹ️ No se generó ningún pedido, por lo tanto no se creó el archivo JSON.");
        }

        leer.close();
    }

    // --- Realizar pedido ---
    private static JSONObject realizarPedido(Fruteria fruteria, Scanner leer) {
        JSONArray productos = new JSONArray();
        boolean seguir = true;
        double total = 0.0;

        while (seguir) {
            System.out.print("Producto a comprar: ");
            String nombreProducto = leer.nextLine();

            System.out.print("Cantidad: ");
            double cantidad;
            try {
                cantidad = Double.parseDouble(leer.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Cantidad inválida.");
                continue;
            }

            double precio = buscarPrecio(fruteria, nombreProducto);
            if (precio != -1) {
                double subtotal = precio * cantidad;
                total = total + subtotal;

                JSONObject producto = new JSONObject();
                producto.put("nombre", nombreProducto);
                producto.put("cantidad", cantidad);
                producto.put("precioUnitario", precio);
                producto.put("subtotal", subtotal);
                productos.add(producto);

                System.out.println("Producto añadido al pedido.");
            } else {
                System.out.println("Producto no encontrado en el catálogo.");
            }

            System.out.print("¿Deseas seguir comprando? (s/n): ");
            String respuesta = leer.nextLine();
            seguir = respuesta.equalsIgnoreCase("s");
        }

        if (productos.isEmpty()) return null;

        JSONObject pedido = new JSONObject();
        pedido.put("productos", productos);
        pedido.put("total", total);
        return pedido;
    }

    // --- Buscar precio ---
    private static double buscarPrecio(Fruteria fruteria, String nombre) {
        for (Fruta f : fruteria.getFrutas()) {
            if (f.getNombre().equalsIgnoreCase(nombre)) return f.getPrecio();
        }
        for (Verdura v : fruteria.getVerduras()) {
            if (v.getNombre().equalsIgnoreCase(nombre)) return v.getPrecio();
        }
        return -1;
    }

// --- Guardar JSON ---
private static void guardarJSON(Fruteria fruteria, String nombreCliente, String email,
                               String calle, String numero, String ciudad, String pais,
                               JSONArray pedidos) {

    JSONObject tienda = new JSONObject();
    tienda.put("nombre", fruteria.getNombre());

    // Frutas
    JSONArray arrayFrutas = new JSONArray();
    for (Fruta f : fruteria.getFrutas()) {
        JSONObject fruta = new JSONObject();
        fruta.put("nombre", f.getNombre());
        fruta.put("precio", f.getPrecio());
        fruta.put("temporada", f.getTemporada());
        fruta.put("oferta", f.getOferta());
        arrayFrutas.add(fruta);
    }
    tienda.put("frutas", arrayFrutas);

    // Verduras
    JSONArray arrayVerduras = new JSONArray();
    for (Verdura v : fruteria.getVerduras()) {
        JSONObject verdura = new JSONObject();
        verdura.put("nombre", v.getNombre());
        verdura.put("precio", v.getPrecio());
        verdura.put("tipo", v.getTipo());
        arrayVerduras.add(verdura);
    }
    tienda.put("verduras", arrayVerduras);

    // Cliente
    JSONObject direccion = new JSONObject();
    direccion.put("calle", calle);
    direccion.put("numero", numero);
    direccion.put("ciudad", ciudad);
    direccion.put("pais", pais);

    JSONObject cliente = new JSONObject();
    cliente.put("nombre", nombreCliente);
    cliente.put("email", email);
    cliente.put("direccion", direccion);
    cliente.put("historialCompras", pedidos);

    JSONArray usuarios = new JSONArray();
    usuarios.add(cliente);

    tienda.put("usuarios", usuarios);

    // 🧠 Generar nombre de archivo dinámico
    String nombreLimpio = nombreCliente.trim().replaceAll("\\s+", "_");
    String nombreArchivo = "Cliente_" + nombreLimpio + "_pedido.json";

    // Guardar en archivo JSON
    try (FileWriter file = new FileWriter(nombreArchivo)) {
        file.write(tienda.toJSONString());
        file.flush();
        System.out.println("\nArchivo '" + nombreArchivo + "' generado con éxito con tu pedido.");
    } catch (IOException e) {
        System.out.println("Error al guardar el archivo JSON: " + e.getMessage());
    }
}
}