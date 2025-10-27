import java.util.*;

public class Ejercicio3 {

    static class Pila {
        private LinkedList<Character> datos = new LinkedList<>();

        public void push(char c) {
            datos.push(c);
        }

        public char pop() {
            return datos.pop();
        }

        public boolean isEmpty() {
            return datos.isEmpty();
        }

        public int size() {
            return datos.size();
        }
    }

    // LinkedList
    static class Cola {
        private LinkedList<Character> datos = new LinkedList<>();

        public void enqueue(char c) {
            datos.offer(c);
        }

        public char dequeue() {
            return datos.poll();
        }

        public boolean isEmpty() {
            return datos.isEmpty();
        }

        public int size() {
            return datos.size();
        }
    }

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);

        System.out.println("Palíndromos");
        System.out.print("Introduce una palabra o frase: ");
        String entrada = leer.nextLine();

        if (entrada == null || entrada.trim().isEmpty()) {
            System.out.println("Entrada vacía. No se puede verificar.");
            return;
        }

        String normalizada = entrada.toLowerCase().replaceAll("[^a-z0-9]", "");

        if (normalizada.isEmpty()) {
            System.out.println("Frase no valida.");
            return;
        }


        Pila pila = new Pila();
        Cola cola = new Cola();


        for (char c : normalizada.toCharArray()) {
            pila.push(c);
            cola.enqueue(c);
        }

        boolean esPalindromo = true;
        int n = pila.size();

        for (int i = 0; i < n; i++) {
            char dePila = pila.pop();
            char deCola = cola.dequeue();

            if (dePila != deCola) {
                esPalindromo = false;
                break;
            }
        }

        if (esPalindromo) {
            System.out.println("La frase es un palindromo!");
        } else {
            System.out.println("La frase no es un palíndromo.");
        }
    }
}