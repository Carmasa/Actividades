import java.util.*;

public class Ejercicio1 {

    public static class Nodo {
        char letra;
        Nodo izq, der;

        Nodo(char letra) {
            this.letra = letra;
            this.izq = null;
            this.der = null;
        }
    }

    private Nodo raiz;

    public Ejercicio1() {

        raiz = new Nodo('A');
        raiz.izq = new Nodo('B');
        raiz.der = new Nodo('C');
        raiz.izq.izq = new Nodo('D');
        raiz.izq.der = new Nodo('E');
        raiz.der.izq = new Nodo('F');
    }

    public static void main(String[] args) {
        Ejercicio1 app = new Ejercicio1();
        Scanner leer = new Scanner(System.in);

        System.out.println("Árbol de Letras");
        System.out.println("        A        ");
        System.out.println("      /   \\      ");
        System.out.println("     B     C     ");
        System.out.println("    / \\   /      ");
        System.out.println("   D   E F       ");
        System.out.print("Introduce una letra a buscar: ");
        String input = leer.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("Entrada vacía. No se proporcionó ninguna letra.");
            return;
        }

        char letraBuscada = input.toUpperCase().charAt(0); // Normalizamos a mayúscula

        List<Character> rutaLetras = new ArrayList<>();
        List<Character> rutaDirecciones = new ArrayList<>();

        boolean encontrada = app.encontrarRutaDFS(app.raiz, letraBuscada, rutaLetras, rutaDirecciones);

        if (encontrada) {
            System.out.println("\nLetra encontrada.");
            System.out.println("Ruta de letras: " + rutaLetras);
            System.out.println("Direcciones:    " + rutaDirecciones);
        } else {
            System.out.println("\nLa letra '" + letraBuscada + "' no se encuentra en el árbol.");
        }
    }

    private boolean encontrarRutaDFS(Nodo nodo, char objetivo, List<Character> letras, List<Character> direcciones) {
        if (nodo == null) {
            return false;
        }

        letras.add(nodo.letra);

        if (nodo.letra == objetivo) {
            return true;
        }

        if (nodo.izq != null) {
            direcciones.add('L');
            if (encontrarRutaDFS(nodo.izq, objetivo, letras, direcciones)) {
                return true;
            }
            direcciones.remove(direcciones.size() - 1);
        }

        if (nodo.der != null) {
            direcciones.add('R');
            if (encontrarRutaDFS(nodo.der, objetivo, letras, direcciones)) {
                return true;
            }
            direcciones.remove(direcciones.size() - 1);
        }

        letras.remove(letras.size() - 1);
        return false;
    }
}