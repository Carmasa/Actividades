import java.util.Scanner;

public class Ejercicio4 {

    static class Pila {
        private class Nodo {
            int dato;
            Nodo siguiente;

            Nodo(int dato) {
                this.dato = dato;
                this.siguiente = null;
            }
        }

        private Nodo tope;
        private int tamaño;

        public Pila() {
            this.tope = null;
            this.tamaño = 0;
        }

        public void push(int valor) {
            Nodo nuevonuevo = new Nodo(valor);
            nuevonuevo.siguiente = tope;
            tope = nuevonuevo;
            tamaño++;
        }

        public int pop() {
            if (isEmpty()) {
                throw new RuntimeException("Subdesbordamiento (underflow): la pila está vacía.");
            }
            int valor = tope.dato;
            tope = tope.siguiente;
            tamaño--;
            return valor;
        }

        public boolean isEmpty() {
            return tope == null;
        }

        public int size() {
            return tamaño;
        }
    }

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);

        System.out.println("=== Conversión Decimal → Binario usando PILA ===");
        System.out.print("Introduce un número entero positivo: ");

        if (!leer.hasNextInt()) {
            System.out.println("Error. Debe ser un número entero.");
            return;
        }

        int numero = leer.nextInt();

        if (numero < 0) {
            System.out.println("El número debe ser positivo o cero.");
            return;
        }

        // Caso especial: 0
        if (numero == 0) {
            System.out.println("El número binario de 0 es: 0");
            return;
        }

        Pila pila = new Pila();
        int temporal = numero;

        // Dividir sucesivamente por 2 y apilar los restos
        while (temporal > 0) {
            int resto = temporal % 2;
            pila.push(resto);
            temporal = temporal / 2;
        }

        // Desapilar para obtener el binario en orden correcto
        StringBuilder binario = new StringBuilder();
        while (!pila.isEmpty()) {
            binario.append(pila.pop());
        }

        System.out.println("El número binario de " + numero + " es: " + binario.toString());
    }
}