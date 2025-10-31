import java.util.*;

public class Ejercicio3Ruta {
    public List<String> nodos;
    public List<Ejercicio3ModoTransporte> modos;
    public double tiempoTotal;

    public Ejercicio3Ruta() {
        this.nodos = new ArrayList<>();
        this.modos = new ArrayList<>();
        this.tiempoTotal = 0.0;
    }

    public void addPaso(String nodo, Ejercicio3ModoTransporte modo, double tiempo) {
        nodos.add(nodo);
        modos.add(modo);
        tiempoTotal += tiempo;
    }

    public int contarTransbordos() {
        if (modos.size() <= 1) return 0;
        int cambios = 0;
        for (int i = 1; i < modos.size(); i++) {
            if (modos.get(i) != modos.get(i - 1)) cambios++;
        }
        return cambios;
    }

    public void imprimir() {
        if (nodos.isEmpty()) {
            System.out.println(" No se encontró ruta.");
            return;
        }

        System.out.println("\nRuta encontrada:");
        System.out.println("Tiempo total: " + String.format("%.1f", tiempoTotal) + " min");
        System.out.println("Transbordos: " + contarTransbordos());
        System.out.println("Secuencia:");

        // Imprimir origen sin modo
        System.out.println("  → " + nodos.get(0));

        // Imprimir cada paso: nodo destino + modo usado para llegar a él
        for (int i = 0; i < modos.size(); i++) {
            System.out.println("     (" + modos.get(i) + ") → " + nodos.get(i + 1));
        }
    }
}