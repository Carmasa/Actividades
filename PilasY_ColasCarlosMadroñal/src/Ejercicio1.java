import java.util.Scanner;

public class Ejercicio1 {

    // Clase interna que implementa una Pila de caracteres
    static class Pila {
        private char[] elementos;
        private int limite;
        private int capacidad;

        // Constructor
        public Pila(int capacidad) {
            this.capacidad = capacidad;
            elementos = new char[capacidad];
            limite = -1;
        }

        public void push(char caracter) {
            if (limite == capacidad - 1) {
                System.out.println("Error: la pila está llena.");
            } else {
                elementos[++limite] = caracter;
            }
        }

        public char pop() {
            if (isEmpty()) {
                System.out.println("Error: la pila está vacía.");
                return '\0';
            } else {
                return elementos[limite--];
            }
        }

        public boolean isEmpty() {
            return limite == -1;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese un texto: ");
        String texto = sc.nextLine();

        Pila pila = new Pila(texto.length());

        for (int i = 0; i < texto.length(); i++) {
            pila.push(texto.charAt(i));
        }

        StringBuilder textoInvertido = new StringBuilder();
        while (!pila.isEmpty()) {
            textoInvertido.append(pila.pop());
        }

        System.out.println("Texto invertido: " + textoInvertido.toString());

        sc.close();
    }
}
